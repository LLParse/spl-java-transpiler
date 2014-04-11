package com.dontexist.morals.visitor;

import java.util.ArrayList;
import java.util.List;

import com.dontexist.morals.splparser.node.Node;

/**
 * 
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class MethodInvocation extends JavaConcept {

   public static final Integer INHERITED = 0;
   public static final Integer DYNAMIC   = 1;
   public static final Integer INVOKED   = 2;

   private NewLine             nl;
   private String              name;
   private List<Node>          inputNodes;
   private List<String>        inputTypes;
   private List<Variable>      outputVars;
   private Integer             mode;

   public MethodInvocation(SPLPrinterVisitor visitor) {
      super(visitor);
      this.nl = new NewLine(visitor);
      this.inputNodes = new ArrayList<Node>();
      this.inputTypes = new ArrayList<String>();
      this.outputVars = new ArrayList<Variable>();
      this.mode = INHERITED;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void addInputNode(Node node, String type) {
      inputNodes.add(node);
      inputTypes.add(type);
   }

   public void addOutputVariable(String name, String type) {
      outputVars.add(new Variable(visitor, name, type));
   }

   public void setMode(int mode) {
      this.mode = mode;
   }

   public void write() {

      if (mode.equals(DYNAMIC)) {
         nl.write();
         buffer.append("try {");
         nl.incLevel();
         nl.write();
         buffer.append("Class<?> clazz = Class.forName(").append(name).append(");");
         nl.write();
         buffer.append("Method method = clazz.getDeclaredMethod(\"execute\"");
         for (int i = 0; i < inputTypes.size(); i++) {
            String type = inputTypes.get(i);
            buffer.append(", ").append(type).append(".class");
         }
         buffer.append(");");

         nl.write();
         if (outputVars.size() > 0) {
            buffer.append(outputVars.get(0).getName()).append(" = (").append(outputVars.get(0).getType()).append(") ");
         }
         buffer.append("method.invoke(");
         for (int i = 0; i < inputNodes.size(); i++) {
            Node input = inputNodes.get(i);
            if (i > 0)
               buffer.append(", ");

            if (input != null)
               // make sure to pass the datatype
               input.jjtAccept(visitor, inputTypes.get(i));
            else
               buffer.append("null");
         }
         buffer.append(");");

         nl.decLevel();
         nl.write();
         buffer.append("}");
         nl.write();
         buffer.append("catch (Exception e) {");
         nl.incLevel();
         nl.write();
         buffer.append("e.printStackTrace();");
         nl.decLevel();
         nl.write();
         buffer.append("}");

      }
      else {
         // left-hand side
         switch (outputVars.size()) {
         case 0:
            break;
         case 1:
            Variable var = outputVars.get(0);
            buffer.append(var.getName()).append(" = ");
            if (var.getType() != null) {
               buffer.append("(").append(var.getType()).append(") ");
            }
            break;
         default:
            buffer.append("Iterator<Object> ").append(visitor.variable("it")).append(" = ");
            break;
         }

         // right-hand side
         if (mode.equals(INHERITED)) {
            buffer.append(name).append("(");
         }
         else {
            buffer.append("new ").append(name).append("().execute(");
         }
         for (int i = 0; i < inputNodes.size(); i++) {
            if (i > 0)
               buffer.append(", ");

            if (inputNodes.get(i) != null)
               inputNodes.get(i).jjtAccept(visitor, inputTypes.get(i));
            else
               buffer.append("null");
         }
         buffer.append(")");
         // get iterator
         if (outputVars.size() > 1) {
            buffer.append(".iterator();");
         }
      }
   }

}
