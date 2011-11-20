/**
 * $Id$
 *
 * 
 */
package org.yuksnort.maven.plugins;

import java.lang.reflect.*;
import java.util.Set;
import org.junit.Test;	
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

/**
 * @author Mike Hudgins <mchudgins@dstsystems.com>
 *
 */
public class Fubar
	{
	
	@Test
	public	void	checkSyntax()
		{
		Object	o	= new Object();
		
		Class[]	classes	= o.getClass().getClasses();
		for ( Class c : classes )
			{
			System.out.println( c.getName() );
			}
		
		Package	p	= o.getClass().getPackage();
		Package[] pkgs	= o.getClass().getPackage().getPackages();
		System.out.println( p.getName() );
		for ( Package ptmp : pkgs )
			{
			System.out.println( ptmp.getName() );
			}
		}
	
	@Test
	public	void	trySpring()
		{
		ClassPathScanningCandidateComponentProvider scanner	= new ClassPathScanningCandidateComponentProvider( true );
		
		Set< BeanDefinition > results	= scanner.findCandidateComponents( "com.dstresearch" );
		System.out.println( results.size() );
		}
	}
