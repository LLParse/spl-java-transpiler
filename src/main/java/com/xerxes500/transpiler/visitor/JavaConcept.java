package com.xerxes500.transpiler.visitor;

/**
 * @author James Oliver
 * @version $Id$
 */
public abstract class JavaConcept {

   SPLPrinterVisitor visitor;
   StringBuffer      buffer;

   public JavaConcept(SPLPrinterVisitor visitor) {
      this.visitor = visitor;
      this.buffer = visitor.buffer;
   }

   /**
    * Encapsulates code generation logic for a specific Java concept.
    */
   public abstract void write();
}
