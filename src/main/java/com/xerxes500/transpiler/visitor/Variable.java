package com.xerxes500.transpiler.visitor;

/**
 * @author James Oliver
 * @version $Id$
 */
public class Variable extends JavaConcept {

   private String name;
   private String type;

   public Variable(SPLPrinterVisitor visitor, String name, String type) {
      super(visitor);
      this.name = name;
      this.type = type;
   }

   public Variable(SPLPrinterVisitor visitor) {
      super(visitor);
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the type
    */
   public String getType() {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(String type) {
      this.type = type;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void write() {
      buffer.append(name);
   }
}
