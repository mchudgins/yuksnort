/**
 * $Id$
 *
 * 
 */
package org.yuksnort.maven.plugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;


/**
 * @author Mike Hudgins <mchudgins@dstsystems.com>
 *
 */
public class Scanner
	{
	private	String	baseName;
	
	public static void main( String[] args )
		{
		Scanner	scanner	= new Scanner( "com.dstresearch" );
		
		scanner.process( args[ 0 ] );
		}
	
	public	Scanner( String pkgName )
		{
		this.baseName	= pkgName;
		}
	
	public	void	process( String warFile )
		{
		ClassPathScanningCandidateComponentProvider scanner	= new ClassPathScanningCandidateComponentProvider( true );
		
		System.out.println( "scanning " + warFile );
		Set< BeanDefinition > results	= scanner.findCandidateComponents( this.baseName );

		System.out.println( results.size() );
		for ( BeanDefinition b : results )
			{
			System.out.println( b.getBeanClassName() );
			System.out.println( b.getClass().getName() );
			
			// TODO:  add runtime check that this cast will work...
			AnnotatedBeanDefinition ab	= (AnnotatedBeanDefinition) b;
			
			AnnotationMetadata meta	= ab.getMetadata();
			Set< String > atypes	= meta.getAnnotationTypes();
			for ( String s : atypes )
				{
				System.out.println( s );
				}
			String reqmap	= "org.springframework.web.bind.annotation.RequestMapping";
			Set< MethodMetadata > metaSet	= meta.getAnnotatedMethods( reqmap );
			for ( MethodMetadata m : metaSet )
				{
				System.out.println( m.getMethodName() );
				Map< String, Object >	attrMap = m.getAnnotationAttributes( reqmap );
				String[] s	= (String[]) attrMap.get( "value" );
				for ( String e : s )
					System.out.println( "value:  " + e );
				
//				RequestMethod[] rm		= (RequestMethod[]) attrMap.get( "method" );
//				for ( RequestMethod reqmeth : rm )
//					System.out.println( "method:  " + e );
				
				}

			}

		}
	}
