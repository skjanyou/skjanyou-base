package com.skjanyou.mavenplugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "touch", defaultPhase = LifecyclePhase.INSTALL)
public class MyMojo extends AbstractMojo
{
	@Parameter(defaultValue = "${project.build.directory}")
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		getLog().info(outputDirectory.toString());
		File f = outputDirectory;

		if ( !f.exists() )
		{
			f.mkdirs();
		}

		File touch = new File( f, "touch.txt" );

		FileWriter w = null;
		try
		{
			w = new FileWriter( touch );

			w.write( "touch.txt" );
		}
		catch ( IOException e )
		{
			throw new MojoExecutionException( "Error creating file " + touch, e );
		}
		finally
		{
			if ( w != null )
			{
				try
				{
					w.close();
				}
				catch ( IOException e )
				{
					// ignore
				}
			}
		}
	}
}
