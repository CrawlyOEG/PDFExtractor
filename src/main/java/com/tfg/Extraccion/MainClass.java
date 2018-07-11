package com.tfg.Extraccion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MainClass {

	public static void main(String[] args) {
		boolean fixText = false;
		boolean resources = false;
		boolean onlyText = true;
		List<RangeExtraction> numerosMarcadores = new ArrayList<>();
		List<RangeExtraction> numerosPaginas = new ArrayList<>();
		ExtractionMode modoExtraccion = ExtractionMode.COMPLETE;
		File outputfolder = null;
		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(buildOptions(), args);
			if (line.hasOption('h') || !line.hasOption('i')) {
				printHelp();
				System.exit(0);
			}
			if(line.hasOption('b') && line.hasOption('p')) {
				System.out.println("¡Not both arguments at the same time!.");
				System.exit(1);
			}
			if (line.hasOption('r')) {
				resources = true;
			}
			String pdfOrFolder = line.getOptionValue('i');
			List<File> misPDF = comprobacionArchivo(pdfOrFolder);
			if(line.hasOption('o')) {
				outputfolder = new File(line.getOptionValue('o'));
				if(!outputfolder.exists()) {
					System.out.println("The output folder doesn´t exits");
					System.exit(1);
				}
				if(!outputfolder.isDirectory()) {
					System.out.println("The output isn´t a folder");
					System.exit(1);
				}
			}
			if(line.hasOption('f'))
				fixText = true;
			if(line.hasOption('b')) {
				onlyText = false;
				if(!line.hasOption('p'))
					modoExtraccion = ExtractionMode.BOOKMARK;
				String nuevo = line.getOptionValue('b');
				numerosMarcadores = comprobacionDeArgumentos(nuevo);
			}
			if(line.hasOption('p')) {
				onlyText = false;
				if(!line.hasOption('b'))
					modoExtraccion = ExtractionMode.PAGES;
				String nuevo = line.getOptionValue('p');
				numerosPaginas = comprobacionDeArgumentos(nuevo);
			}
			for (File file : misPDF) {
				try {
					if(resources) {
						System.out.println("Extracting all resouces from " + file.getName());
						new ReadPDF(file,modoExtraccion,numerosMarcadores,numerosPaginas,fixText,outputfolder,false).run();
					}
					else {
						System.out.println("Extracting text from the PDF " + file.getName());
						new ReadPDF(file,modoExtraccion,numerosMarcadores,numerosPaginas,fixText,outputfolder,onlyText).run();
					}
				} catch (IOException | ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		}catch (ParseException exp) {
			exp.printStackTrace();
			System.err.println("Error: " + exp.getMessage());
			System.exit(1);
		}
	}

	public static Options buildOptions() {
		Options o = new Options();
		o.addOption("h", "help", false, "Indicate how yo use the program.");
		o.addOption("f", "fix", false, "[EXPERIMENTAL] Force PDF to be extracted adjunting words, deleting files, deleting footers, .. By default, disabled");
		o.addOption("r", "resources", false, "Try to extract all resources from PDF (text, image and tables). By default, disabled");
		o.addOption(Option.builder("i")
				.longOpt("input")
				.desc("[REQUIRED] Absolute Pdf or folder with PDF location path. Ex: /Users/thoqbk/table.pdf")
				.hasArg()
				.argName("input PDF or FOLDER")
				.build());
		o.addOption(Option.builder("o")
				.longOpt("output")
				.desc("Absolute output file. By default the folder on i or the parent. Ex: /Users/thoqbk/results")
				.hasArg()
				.argName("output FOLDER")
				.build());
		o.addOption(Option.builder("p")
				.longOpt("pages")
				.desc("[OPTIONAL] ¡NOT AT SAME THAN -p! By default, the extractor extract all of them. \n"
						+ "Using comma separated or list of ranges to list to select pages"
						+ "Examples: --pages 1-3,5-7, --pages 3.\n")
				.hasArg()
				.argName("NUMBERS")
				.build());
		o.addOption(Option.builder("b")
				.longOpt("bookmark")
				.desc("[OPTIONAL] ¡NOT AT SAME THAN -p! By default, the extractor extract all of them. \n"
						+ "If the PDF has BOOKMARKS, we extract all content from selected. Using comma separated or list of ranges to list"
						+ "Examples: --bookmark 1-3,5-7, --bookmark 3.\n")
				.hasArg()
				.argName("NUMBERS")
				.build());
		return o;
	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("PDFExtractor", "Mised argument", buildOptions(), "", true);
	}

	private static List<File> comprobacionArchivo(String archivo) {
		File pdfOrFolder = null;
		try {
			pdfOrFolder = new File(archivo);
			if(!pdfOrFolder.exists()) {
				System.out.println("The input file or folder doesn´t exist.");
				System.exit(1);
			}
		}catch(java.lang.NullPointerException e) {
			System.out.println("The input file or folder doesn´t exist.");
			System.exit(1);
		}
		List<File> aDevolver = new ArrayList<>();
		if(pdfOrFolder.isDirectory()) {
			for (File isAPDF : pdfOrFolder.listFiles()) {
				String nombreArchivo = isAPDF.getName();
				try {
					String extension = isAPDF.getName().substring(nombreArchivo.length()-4, nombreArchivo.length());
					if(extension.equalsIgnoreCase(".pdf")){
						aDevolver.add(isAPDF); 
					}
				}catch(java.lang.StringIndexOutOfBoundsException e) {
					//Para evitar archivos con longitud menor a tres caracteres
				}
			}
			if(aDevolver.isEmpty()) {
				System.out.println("The folder doesn´t contains a PDF.");
				System.exit(1);
			}
			return aDevolver; //Es un directorio
		}
		else {
			String nombreArchivo = pdfOrFolder.getName();
			try {
				String extension = nombreArchivo.substring(nombreArchivo.length()-4, nombreArchivo.length());
				if(extension.equals(".pdf")){
					aDevolver.add(pdfOrFolder);
					return aDevolver;
				}
			}catch(java.lang.StringIndexOutOfBoundsException e) {
				//Para evitar archivos con longitud menor a tres caracteres
			}
			aDevolver.add(pdfOrFolder);
			return aDevolver;
		}
	}

	private static List<RangeExtraction> comprobacionDeArgumentos(String auxiliar) {
		List<RangeExtraction> numbers = new ArrayList<>();
		String[] arguments = auxiliar.split(",");
		for (String string : arguments) {
			if(string.contains("-")) {
				String[] listaNumeros = string.split("-");
				//El programa no permite rango multiple al menos que este separado por una coma
				if(listaNumeros.length != 2) {
					System.out.println("The bookmark or page introduced is not a number");
					System.exit(1);
				}
				try {
					int numeroInicial = Integer.parseInt(listaNumeros[0]);
					int numeroFinal = Integer.parseInt(listaNumeros[1]);
					if(numeroFinal < numeroInicial) {
						System.out.println("The bookmark or page introduced is not a number");
						System.exit(1);
					}
					RangeExtraction rango = new RangeExtraction(numeroInicial,numeroFinal);
					numbers.add(rango);
				}catch(java.lang.NumberFormatException e) {
					System.out.println("The bookmark or page introduced is not a number");
					System.exit(1);
				}
			}
			else {
				try {
					int numeroInicial = Integer.parseInt(string);
					RangeExtraction rango = new RangeExtraction(numeroInicial);
					numbers.add(rango);
				}catch(java.lang.NumberFormatException e) {
					System.out.println("The bookmark or page introduced is not a number");
					System.exit(1);
				}
			}
		}
		return numbers;
	}
}
