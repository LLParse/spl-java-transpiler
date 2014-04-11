package com.dontexist.morals.visitor;

/**
 * 
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class WhiteSpace extends JavaConcept {

   /**
    * @param visitor
    */
   public WhiteSpace(SPLPrinterVisitor visitor) {
      super(visitor);
   }

   private static final String INDENT  = "    ";
   private static int          level   = 0;

   public void reset() {
      level = 0;
   }

   public void incLevel() {
      level++;
   }

   public void decLevel() {
      level--;
   }

   public int getLevel() {
      return level;
   }

   @Deprecated
   public String indentString() {
      StringBuffer buff = new StringBuffer();
      for (int i = 0; i < level; i++)
         buff.append(INDENT);
      return buff.toString();
   }

   /**
   * {@inheritDoc}
   */
   @Override
   public void write() {
      for (int i = 0; i < level; i++) {
         buffer.append(INDENT);
      }
   }
}
