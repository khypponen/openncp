/**
 * *Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *//*
	* To change this template, choose Tools | Templates
	* and open the template in the editor.
	*/
package epsos.ccd.gnomon.xslt;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 *
 * @author karkaletsis
 */
public class DisplayTest {
	private static final Logger logger = Logger.getLogger(DisplayTest.class);

	private enum TRANSFORMATION {
		WithOutputAndUserHomePath, ForPDF, UsingStandardCDAXsl, WithOutputAndDefinedPath
	};
	
	
//	@Test
	private void fileTest(String input, TRANSFORMATION type)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();
		logger.info("Transforming file: " + input);

		String cda = "";
		try {
			cda = xlsClass.readFile(input);
		} catch (Exception e) {
			System.out.println("File not found");
		}
		String out = "";
		switch (type) {
		case ForPDF:
			out = xlsClass.transformForPDF(cda, "el_GR", false);
			break;
		case UsingStandardCDAXsl:
			out = xlsClass.transformUsingStandardCDAXsl(cda);
		case WithOutputAndDefinedPath:
			out = xlsClass.transformWithOutputAndDefinedPath(cda, "el_GR", "", Paths.get(System.getenv("EPSOS_PROPS_PATH"), "EpsosRepository"));
		case WithOutputAndUserHomePath:
			out = xlsClass.transformWithOutputAndUserHomePath(cda, "el_GR", "");

		}
		String filename = Paths.get(input).getFileName().toString();
		String stripExt = filename.substring(0, filename.lastIndexOf("."));
		String pt = Paths.get(System.getenv("EPSOS_PROPS_PATH"), "EpsosRepository", "out", stripExt + ".html")
				.toString();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pt), "utf-8"))) {
			writer.write(out);
		}

		// xlsClass.transformForPDF(cda, "el-GR",true);

	}

//	@Test
	private void folderTest(String input, final TRANSFORMATION type) {
		Path path = Paths.get(input);
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (!attrs.isDirectory()) {
						fileTest(file.toString(), type);
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void runFile() throws Exception {
		// Vaccination
		// fileTest("samples/cda_xml_157.xml");
		// fileTest("samples/epSOS_MRO_test_full.xml");
		// fileTest("samples/epSOS_RTD_PS_EU_Pivot_CDA_Paolo.xml");
		// fileTest("samples/es_ps_pivot.xml");

		// Frequency
		fileTest("samples/multiingredient.xml", TRANSFORMATION.WithOutputAndUserHomePath);

	}

	
	@Test 
	public void readFile() throws Exception{
		EpsosXSLTransformer xlsClass = new EpsosXSLTransformer();
		String out = xlsClass.readFile("samples/multiingredient.xml");
		String pt = Paths.get(System.getenv("EPSOS_PROPS_PATH"), "EpsosRepository", "out", "readfile.txt")
				.toString();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pt), "utf-8"))) {
			writer.write(out);
		}
	}
	
//	@Test
//	public void runFolder() throws Exception {
//		folderTest("samples", TRANSFORMATION.WithOutputAndUserHomePath);
//	}
}
