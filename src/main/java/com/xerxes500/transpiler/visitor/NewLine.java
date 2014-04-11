package com.xerxes500.transpiler.visitor;

/**
 * @author James Oliver
 * @version $Id$
 */
public class NewLine extends WhiteSpace {

   private static final String NEW_LINE = System.getProperty("line.separator");
   /**
    * @param visitor
    */
   public NewLine(SPLPrinterVisitor visitor) {
      super(visitor);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void write() {
      buffer.append(NEW_LINE);
      super.write();
   }

}
