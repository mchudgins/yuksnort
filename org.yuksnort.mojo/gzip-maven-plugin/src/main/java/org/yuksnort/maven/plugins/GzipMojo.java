/**
 * $Id$
 *
 * 
 */
package org.yuksnort.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * @author Mike Hudgins <mchudgins@dstsystems.com>
 *
 * @phase prepare-package
 * @goal gzip
 * @threadSafe
 */

public class GzipMojo extends AbstractMojo
	{
	
	public	class DirectoryWalker
		{
		private	String[]	exts;
		private	List< File >	matches	= new ArrayList( 20 );
		
		public DirectoryWalker( List< String > fileExt )
			{
			this.exts	= (String[]) fileExt.toArray( new String[ fileExt.size() ] );
			for ( int i = 0; i < this.exts.length; i++ )
				{
				String	e	= this.exts[ i ];
				if ( ! e.startsWith( "." ) )
					{
					e	= "." + e;
					this.exts[ i ] = e;
					}
				}
			}
		
		protected boolean extMatches( File g )
			{
			for ( String s : this.exts )
				{
				if ( g.toString().endsWith( s ) )
					return( true );
				}
			return( false );
			}
		
		public	void	findMatches( File f )
			{
			if ( f.isDirectory() )
				{
				for ( File g : f.listFiles() )
					{
					if ( g.isDirectory() )
						{
						findMatches( g );
						}
					else if ( g.canRead() && g.isFile() && extMatches( g ) )
						{
						this.matches.add( g );
						}
					}
				}
			}
		
		public	List< File >	getMatches()
			{
			return( this.matches );
			}
		}

	/**
	 * List of subdir's to gzip contents
	 * 
	 * @parameter expression="${gzip.gzip}" default-value="nothing, yet."
	 */
	private	String	gzip;
	
	/**
	 * @parameter expression="${webappDirectory}" default-value="${project.build.directory}/${project.build.finalName}"
	 */
	private String webapp;
	
	/**
	 * @parameter expression="${warName}" default-value="${project.build.directory}/${project.build.finalName}.war"
	 */
	private String warName;
	
	/**
	 * @parameter default-value="${project.build.directory}"
	 */
	private String buildDir;
	
	/**
	 * The list of file extensions we want to compress.
	 *
	 * @parameter expression="${gzip.extensions}"
	 * @required
	 * @readonly
	 */
	private List< String > fileExtensions;
	
	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException
		{
		
		if ( this.fileExtensions.size() == 0 )
			{
			this.getLog().warn( "No file extensions configured.  Nothing to GZip." );
			return;
			}
		
		DirectoryWalker	dw	= new DirectoryWalker( this.fileExtensions );
		dw.findMatches( new File( this.webapp ) );
		
		for ( File f : dw.getMatches() )
			{
			compress( f );
			}
		
		Scanner scanner	= new Scanner( "com.dstresearch" );
		scanner.process( this.buildDir + "/classes" );
		}
	
	protected void compress( File f )
		{
		try
			{
			int			bytesRead;
			byte[]			buf	= new byte[ 4096 * 4 ];
			File			fgz	= new File( f.toString() + ".gz" );
			if ( fgz.exists() && fgz.lastModified() > f.lastModified() )
				return;

			if ( ! fgz.exists() )
				fgz.createNewFile();

			this.getLog().info( "gzipping " + f.toString() );
			FileInputStream		in	= new FileInputStream( f );
			FileOutputStream	out	= new FileOutputStream( fgz );
			GZIPOutputStream	outgz	= new GZIPOutputStream( out, buf.length );
			
			while ( ( bytesRead = in.read( buf ) ) != -1 )
				outgz.write( buf, 0, bytesRead );
			
			outgz.finish();
			outgz.close();
			out.close();
			in.close();
			}
		catch ( FileNotFoundException e )
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch ( IOException e )
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
