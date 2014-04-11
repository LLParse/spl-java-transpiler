package com.dontexist.morals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.dontexist.morals.splparser.ParseException;
import com.dontexist.morals.splparser.SPLParser;
import com.dontexist.morals.splparser.node.ASTCompilationUnit;
import com.dontexist.morals.symbol.SymbolVisitor;
import com.dontexist.morals.symbol.Symbols;
import com.dontexist.morals.visitor.SPLPrinterVisitor;

/**
 * 
 * <p>
 * 'Poor flying bald guy' encapsulates the true purpose of this software being developed - 
 * to shave my boss' head and push him out of an airplane. Best think twice before playing.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class PoorFlyingBaldGuy {

   // Key is the class name, value is corresponding AST
   private static Map<String, ASTCompilationUnit> ASTMap          = new TreeMap<String, ASTCompilationUnit>();
   private static Logger                          logger          = Logger.getLogger(PoorFlyingBaldGuy.class);
   private static SPLParser                       parser          = null;

   // program arguments
   private static String                          inputDir        = null;
   private static String                          outputPackage   = null;
   private static String                          outputDir       = null;
   private static Boolean                         ignoreExtension = Boolean.FALSE;

   public static void main(String[] args) {

      configureLogger();

      // validate arg count
      if (args.length != 2 && args.length != 3)
         printUsage();

      // read arguments
      inputDir = args[0]; // ex. src/main/resources/spl
      outputPackage = args[1]; // ex. com.choicehotels.cis.spl.gen
      outputDir = "src/main/java/".concat(outputPackage.replaceAll("\\.", "/"));
      ignoreExtension = args.length > 2 ? Boolean.valueOf(args[2]) : Boolean.FALSE;

      logger.debug("-----Input Arguments-----");
      logger.debug("Input Directory: " + inputDir);
      logger.debug("Output Package: " + outputPackage);
      logger.debug("Output Directory: " + outputDir);
      logger.debug("Ignore non-SQL Extensions: " + ignoreExtension);

      try {
         Symbols symbols = gatherSymbols();
         translate(symbols);
      }
      catch (ParseException e) {
         logger.fatal("Parse exception!");
         e.printStackTrace();
      }

   }

   public static Symbols gatherSymbols() throws ParseException {
      Symbols symbols = new Symbols();
      try {
         ASTCompilationUnit node = null;
         for (File file : new File(inputDir).listFiles()) {

            // skip non-sql files
            if (!ignoreExtension && !file.getName().endsWith(".sql"))
               continue;

            String name = file.getName();
            logger.info("Gathering cheesypoofs: " + name);
            name = name.substring(0, name.length() - 4);
            name = normalize(name);

            // load file into parser
            initParser(file);

            // create the AST and get root
            node = SPLParser.CompilationUnit();

            // save AST for further visitors
            ASTMap.put(name, node);

            // Instantiate symbol visitor and visit the AST
            SymbolVisitor visitor = new SymbolVisitor();
            symbols = (Symbols) node.jjtAccept(visitor, symbols);
         }
      }
      catch (IOException e) {
         logger.fatal("Unable to read file!");
         e.printStackTrace();
      }
      return symbols;
   }

   public static void translate(Symbols symbols) {
      ASTCompilationUnit node = null;
      Iterator<Entry<String, ASTCompilationUnit>> it = ASTMap.entrySet().iterator();
      while (it.hasNext()) {
         String className = null;
         try {
            Entry<String, ASTCompilationUnit> entry = it.next();
            className = entry.getKey();
            node = entry.getValue();

            logger.info("Writing: " + className + ".java");

            SPLPrinterVisitor visitor = new SPLPrinterVisitor(className, outputPackage);
            String result = (String) node.jjtAccept(visitor, symbols);

            // Write the result and close the writer
            String outName = outputDir.concat("/").concat(className).concat(".java");
            writeFile(outName, result);
         }
         catch (IOException e) {
            logger.error("Error visiting AST for: " + className);
            e.printStackTrace();
         }
      }
   }

   private static void configureLogger() {
      ConsoleAppender console = new ConsoleAppender(); //create appender
      //configure the appender
      String PATTERN = "%r\t[%p] %C{1} - %m%n";
      console.setLayout(new PatternLayout(PATTERN));
      console.setThreshold(Level.INFO);
      console.activateOptions();
      //add appender to any Logger (here is root)
      Logger.getRootLogger().addAppender(console);
   }

   private static void printUsage() {
      ResourceBundle bundle = ResourceBundle.getBundle("version");
      String version = bundle.getString("project.version");
      logger.error("SPLTranslate v" + version + " - Informix v11.7 Stored Procedure Langauge (SPL) to Java Translation Tool");
      logger.error("");
      logger.error("Usage:  java " + PoorFlyingBaldGuy.class.getName() + " inputDir outputPackage [sqlExtensions=true]");
      logger.error("\t\tinputDir\t- The source directory to translate");
      logger.error("\t\toutputPackage\t- The Java package to store the translated source code");
      logger.error("\t\tsqlExtensions\t- Default true. Enables or disables processing files without a .sql extension");
      System.exit(255);
   }

   public static void initParser(File file) throws FileNotFoundException {
	   initParser(new FileInputStream(file.getAbsoluteFile()));
   }
   
   public static void initParser(String file) {
	   initParser(new ByteArrayInputStream(file.getBytes()));
   }
   
   public static void initParser(InputStream stream) {
      if (parser == null)
          parser = new SPLParser(stream);
       else
          SPLParser.ReInit(stream);
   }

   public static void writeFile(String path, String content) throws IOException {
      File file = new File(path);
      file.getParentFile().mkdirs();
      PrintWriter out = new PrintWriter(new FileWriter(file));
      out.write(content);
      out.close();
   }

   public static String camelCase(String input) {
      StringBuffer output = new StringBuffer();
      boolean capital = true;

      for (int i = 0; i < input.length(); i++) {

         if (input.charAt(i) == '_') {
            capital = true;
            continue;
         }

         if (capital == true) {
            output.append(Character.toUpperCase(input.charAt(i)));
            capital = false;
         }
         else {
            output.append(input.charAt(i));
         }
      }
      return output.toString();
   }
   
   public static String normalize(String obj) {
      return obj.replaceAll("\\-", "_");
   }
}
