package com.xerxes500.transpiler.visitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xerxes500.transpiler.splparser.Token;
import com.xerxes500.transpiler.splparser.node.ASTAggregateFunction;
import com.xerxes500.transpiler.splparser.node.ASTArgument;
import com.xerxes500.transpiler.splparser.node.ASTArgumentList;
import com.xerxes500.transpiler.splparser.node.ASTAssignmentStatement;
import com.xerxes500.transpiler.splparser.node.ASTAssignmentStatementList;
import com.xerxes500.transpiler.splparser.node.ASTBlockStatement;
import com.xerxes500.transpiler.splparser.node.ASTCompilationUnit;
import com.xerxes500.transpiler.splparser.node.ASTCompoundStatement;
import com.xerxes500.transpiler.splparser.node.ASTContinueLoopStatement;
import com.xerxes500.transpiler.splparser.node.ASTCreateTableStatement;
import com.xerxes500.transpiler.splparser.node.ASTDeclaration;
import com.xerxes500.transpiler.splparser.node.ASTDefinitionStatement;
import com.xerxes500.transpiler.splparser.node.ASTDeleteClause;
import com.xerxes500.transpiler.splparser.node.ASTDeleteExpression;
import com.xerxes500.transpiler.splparser.node.ASTElifClause;
import com.xerxes500.transpiler.splparser.node.ASTElseClause;
import com.xerxes500.transpiler.splparser.node.ASTExitLoopStatement;
import com.xerxes500.transpiler.splparser.node.ASTExpressionList;
import com.xerxes500.transpiler.splparser.node.ASTExpressionStatement;
import com.xerxes500.transpiler.splparser.node.ASTForEachStatement;
import com.xerxes500.transpiler.splparser.node.ASTForStatement;
import com.xerxes500.transpiler.splparser.node.ASTFromClause;
import com.xerxes500.transpiler.splparser.node.ASTFromExpression;
import com.xerxes500.transpiler.splparser.node.ASTFromExpressionList;
import com.xerxes500.transpiler.splparser.node.ASTGroupByClause;
import com.xerxes500.transpiler.splparser.node.ASTIdentifier;
import com.xerxes500.transpiler.splparser.node.ASTIdentifierList;
import com.xerxes500.transpiler.splparser.node.ASTIfClause;
import com.xerxes500.transpiler.splparser.node.ASTInsertClause;
import com.xerxes500.transpiler.splparser.node.ASTInsertExpression;
import com.xerxes500.transpiler.splparser.node.ASTIntoClause;
import com.xerxes500.transpiler.splparser.node.ASTLiteral;
import com.xerxes500.transpiler.splparser.node.ASTLockTableExpression;
import com.xerxes500.transpiler.splparser.node.ASTNestedExpression;
import com.xerxes500.transpiler.splparser.node.ASTOnExceptionStatement;
import com.xerxes500.transpiler.splparser.node.ASTOrderByClause;
import com.xerxes500.transpiler.splparser.node.ASTOrderByExpression;
import com.xerxes500.transpiler.splparser.node.ASTOrderByExpressionList;
import com.xerxes500.transpiler.splparser.node.ASTPrimitiveType;
import com.xerxes500.transpiler.splparser.node.ASTPrimitiveTypeList;
import com.xerxes500.transpiler.splparser.node.ASTProcedureConclusion;
import com.xerxes500.transpiler.splparser.node.ASTProcedureDeclaration;
import com.xerxes500.transpiler.splparser.node.ASTRaiseExceptionStatement;
import com.xerxes500.transpiler.splparser.node.ASTReturnStatement;
import com.xerxes500.transpiler.splparser.node.ASTRevokeDeclaration;
import com.xerxes500.transpiler.splparser.node.ASTSQLStatement;
import com.xerxes500.transpiler.splparser.node.ASTSelectClause;
import com.xerxes500.transpiler.splparser.node.ASTSelectExpression;
import com.xerxes500.transpiler.splparser.node.ASTSetClause;
import com.xerxes500.transpiler.splparser.node.ASTSetDebugFileStatement;
import com.xerxes500.transpiler.splparser.node.ASTSetExpression;
import com.xerxes500.transpiler.splparser.node.ASTSetExpressionList;
import com.xerxes500.transpiler.splparser.node.ASTTableDecl;
import com.xerxes500.transpiler.splparser.node.ASTTableDeclList;
import com.xerxes500.transpiler.splparser.node.ASTTraceArgument;
import com.xerxes500.transpiler.splparser.node.ASTTraceStatement;
import com.xerxes500.transpiler.splparser.node.ASTTransactionStatement;
import com.xerxes500.transpiler.splparser.node.ASTUpdateClause;
import com.xerxes500.transpiler.splparser.node.ASTUpdateExpression;
import com.xerxes500.transpiler.splparser.node.ASTValuesClause;
import com.xerxes500.transpiler.splparser.node.ASTWhereClause;
import com.xerxes500.transpiler.splparser.node.ASTWhileStatement;
import com.xerxes500.transpiler.splparser.node.ASTadditiveExpression;
import com.xerxes500.transpiler.splparser.node.ASTandExpression;
import com.xerxes500.transpiler.splparser.node.ASTassignmentExpression;
import com.xerxes500.transpiler.splparser.node.ASTcomparisonExpression;
import com.xerxes500.transpiler.splparser.node.ASTconcatenationExpression;
import com.xerxes500.transpiler.splparser.node.ASTdropTableExpression;
import com.xerxes500.transpiler.splparser.node.ASTintervalParam;
import com.xerxes500.transpiler.splparser.node.ASTintervalParams;
import com.xerxes500.transpiler.splparser.node.ASTmultiplicativeExpression;
import com.xerxes500.transpiler.splparser.node.ASTnotExpression;
import com.xerxes500.transpiler.splparser.node.ASTorExpression;
import com.xerxes500.transpiler.splparser.node.ASTpostfixExpression;
import com.xerxes500.transpiler.splparser.node.ASTprepositionExpression;
import com.xerxes500.transpiler.splparser.node.ASTquantitativeExpression;
import com.xerxes500.transpiler.splparser.node.ASTsubstringExpression;
import com.xerxes500.transpiler.splparser.node.ASTunaryExpression;
import com.xerxes500.transpiler.splparser.node.ASTunitsExpression;
import com.xerxes500.transpiler.splparser.node.Node;
import com.xerxes500.transpiler.splparser.node.SPLParserVisitor;
import com.xerxes500.transpiler.splparser.node.SimpleNode;
import com.xerxes500.transpiler.symbol.Symbols;
import com.xerxes500.transpiler.symbol.Symbols.ProcedureInformation;

/**
 * Visits each node in an Informix 11.7 Stored Procedure Language (SPL) 
 * Abstract Syntax Tree (AST) and produces behaviorally equivalent java source code.
 * <p>
 * Dependencies:
 * <ul>
 * <li><b>AbstractProcedure.class</b> encapsulates helper methods that closely parallel built-in
 * SQL operations</li>
 * </ul>
 * 
 * @author James Oliver
 * @version $Id$
 */
public class SPLPrinterVisitor implements SPLParserVisitor {

   private static Logger                      logger       = Logger.getLogger(SPLPrinterVisitor.class.getName());
   protected Map<String, Map<String, String>> procedureMap = new HashMap<String, Map<String, String>>();

   // Maintains a symbol table for the variables used in each procedure
   // Key: variable name
   // Value: variable type
   protected Map<String, String>              symbolMap    = new HashMap<String, String>();

   // Maintains an index for each variable prefix so we don't end up with conflicting names.
   // The prefix is intended to be concatenated with the index
   protected Map<String, Integer>             varIndexMap  = new HashMap<String, Integer>();

   protected ProcedureInformation             pi;

   // Maintains a symbol table for the tables and their handles in each SQL statement
   // Key: table name
   // Value: table handle
   protected Map<String, String>              sqlSymbolMap = new HashMap<String, String>();

   protected List<String>                     sqlParamList = new ArrayList<String>();

   protected Symbols                          symbols      = null;

   // Stores generated code
   protected StringBuffer                     buffer       = null;
   // Binds to buffer and manages whitespace
   private WhiteSpace                         ws           = null;
   private NewLine                            nl           = null;

   protected static final boolean             autobox      = false;
   protected boolean                          explicitOOP  = false;
   private boolean                            sqlMode      = false;
   protected String                           className    = null;
   private String                             packageName  = null;

   protected ASTIntoClause                    intoClause   = null;
   protected Integer                          startIndex   = -1;
   protected boolean                          inString     = false;

   public SPLPrinterVisitor(String className, String packageName) {
      this.className = className;
      this.packageName = packageName;
      buffer = new StringBuffer();
      ws = new WhiteSpace(this);
      nl = new NewLine(this);
      ws.reset();
   }

   public String variable(String prefix, boolean increment) {
      Integer idx = 0;
      if (varIndexMap.containsKey(prefix)) {
         idx = varIndexMap.get(prefix);
      }

      if (increment) {
         idx++;
      }

      varIndexMap.put(prefix, idx);
      return prefix.concat(idx.toString());
   }

   public String variable(String prefix) {
      return variable(prefix, false);
   }

   /**
    * Assumption: indent variable will always accurately reflect scope
    */
   private Map<Integer, Integer> pstmtScope = new HashMap<Integer, Integer>();
   private Map<Integer, Integer> rsScope = new HashMap<Integer, Integer>();

   private void openStatement() {
      nl.write();
      buffer.append("PreparedStatement ").append(variable("pstmt", true));
      pstmtScope.put(ws.getLevel(), varIndexMap.get("pstmt"));
   }

   private void closeStatement() {
      Integer idx = pstmtScope.remove(ws.getLevel());
      if (idx != null) {
         nl.write();
         buffer.append("pstmt").append(idx.toString()).append(".close();");
      }
   }

   private void openResultSet() {
      nl.write();
      buffer.append("ResultSet ")
            .append(variable("rs", true))
            .append(" = ")
            .append("executeQuery(")
            .append(variable("pstmt"))
            .append(");");
      rsScope.put(ws.getLevel(), varIndexMap.get("rs"));
   }

   private void closeResultSet() {
      Integer idx = rsScope.remove(ws.getLevel());
      nl.write();
      if (idx != null) {
         buffer.append("rs").append(idx.toString()).append(".close();");
      }
   }

   private <T> boolean hasParent(SimpleNode node, Class<T> parentType) {
      boolean hasParent = false;
      SimpleNode parent = null;
      while ((parent = (SimpleNode) node.jjtGetParent()) != null) {
         if (parentType.isInstance(node)) {
            hasParent = true;
            break;
         }
      }
      return hasParent;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(SimpleNode node, Object data) {
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTCompilationUnit node, Object data) {
      symbols = (Symbols) data;

      SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
      Date now = new Date();

      buffer.append("/* Generated on ").append(sdf.format(now)).append(" by SPLParser v0.9 */");
      nl.write();
      buffer.append("package ").append(packageName).append(";");
      nl.write();
      nl.write();
      buffer.append("import java.lang.reflect.Method;");
      nl.write();
      buffer.append("import java.sql.Date;");
      nl.write();
      buffer.append("import java.sql.PreparedStatement;");
      nl.write();
      buffer.append("import java.sql.ResultSet;");
      nl.write();
      buffer.append("import java.sql.SQLException;");
      nl.write();
      buffer.append("import java.sql.Types;");
      nl.write();
      buffer.append("import java.util.ArrayList;");
      nl.write();
      buffer.append("import java.util.Arrays;");
      nl.write();
      buffer.append("import java.util.Collection;");
      nl.write();
      buffer.append("import java.util.Iterator;");
      nl.write();
      nl.write();
      buffer.append("import org.joda.time.DateTime;");
      buffer.append("import org.joda.time.LocalDate;");
      nl.write();
      nl.write();
      buffer.append("import com.xerxes500.transpile.AbstractProcedure;");
      nl.write();
      buffer.append("import com.xerxes500.transpile.ProcedureException;");
      nl.write();
      nl.write();
      buffer.append("public class ").append(className).append(" extends AbstractProcedure {");
      ws.incLevel();
      data = node.childrenAccept(this, data);
      ws.decLevel();
      nl.write();
      nl.write();
      buffer.append("}");

      return buffer.toString();
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTDeclaration node, Object data) {
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTProcedureDeclaration node, Object data) {
      // initialize symbol table
      symbolMap.clear();
      // initialize variable index
      varIndexMap.clear();
      // store a reference to the current procedure symbols
      pi = symbols.getProcedureInfo(node.getName());

      // we need to determine if there are 0, 1 or more return args

      // try to resolve a PrimitiveTypeList node
      ASTPrimitiveTypeList returnArgs = null;
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {

         if (node.jjtGetChild(i) instanceof ASTPrimitiveTypeList) {
            returnArgs = (ASTPrimitiveTypeList) node.jjtGetChild(i);
            break;
         }
      }

      // declare public modifier
      nl.write();
      nl.write();
      buffer.append("@SuppressWarnings(\"unused\")");
      nl.write();
      buffer.append("public ");

      if (returnArgs == null) {
         buffer.append("void ");
      }
      // Returns a single argument
      else if (returnArgs.jjtGetNumChildren() == 1) {
         ASTPrimitiveType type = (ASTPrimitiveType) returnArgs.jjtGetChild(0);

         buffer.append(type.getType()).append(" ");
      }
      // Returns a collection of generic objects
      else {
         buffer.append("Collection<Object> ");
      }

      // write procedure name
      buffer.append("execute");

      // resolve arguments
      ASTArgumentList list = null;
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {

         if (node.jjtGetChild(i) instanceof ASTArgumentList) {
            list = (ASTArgumentList) node.jjtGetChild(i);
            break;
         }
      }
      data = list.jjtAccept(this, data);
      buffer.append(" throws SQLException, ProcedureException {");

      // try to resolve a ASTCompoundStatement node
      ASTCompoundStatement stmt = null;
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {

         if (node.jjtGetChild(i) instanceof ASTCompoundStatement) {
            stmt = (ASTCompoundStatement) node.jjtGetChild(i);
            break;
         }
      }
      ws.incLevel();
      data = stmt.jjtAccept(this, data);
      ws.decLevel();

      nl.write();
      buffer.append("}");

      // store procedure symbols for later use
      procedureMap.put(node.getName(), symbolMap);

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTProcedureConclusion node, Object data) {
      buffer.append("}\n\n");
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTCompoundStatement node, Object data) {
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTArgumentList node, Object data) {
      buffer.append("(");
      data = visitList(node, data);
      buffer.append(")");

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTArgument node, Object data) {
      ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
      ASTPrimitiveType type = (ASTPrimitiveType) node.jjtGetChild(1);

      // store variable definition in the symbol table
      symbolMap.put(ident.getName(), type.getType());

      //write(node);
      data = type.jjtAccept(this, data);
      buffer.append(" ");
      data = ident.jjtAccept(this, data);

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTPrimitiveTypeList node, Object data) {
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTPrimitiveType node, Object data) {
      buffer.append(node.getType());
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTIdentifier node, Object data) {
      String rightHand = null;

      if (data instanceof String && symbolMap.containsKey(node.getName())) {
         String leftHand = (String) data;
         rightHand = symbolMap.get(node.getName());

         if (!leftHand.equals(rightHand)) {
            logger.warn("Left-hand: " + leftHand);
            logger.warn("Right-hand: " + rightHand);
         }
         // map double to integer
         if (leftHand.equals("Double") && rightHand.equals("Integer")) {
            if (node.getModifier() != null)
               buffer.append(node.getModifier()).append(" ");
            buffer.append(node.getName());
            buffer.append(".doubleValue()");
         }
         else if (leftHand.equals("Integer") && rightHand.equals("Double")) {
            if (node.getModifier() != null)
               buffer.append(node.getModifier()).append(" ");
            buffer.append(node.getName());
            buffer.append(".intValue()");
         }
         else if (leftHand.equals("Integer") && rightHand.equals("String")) {
            if (node.getModifier() != null)
               buffer.append(node.getModifier()).append(" ");
            
            buffer.append("Integer.parseInt(")
                  .append(node.getName())
                  .append(")");
         }
         else {
            if (node.getModifier() != null)
               buffer.append(node.getModifier()).append(" ");
            buffer.append(node.getName());
         }
      }
      // if in sql mode and var is defined, parameterize and push var to stack
      else if (sqlMode && symbolMap.containsKey(node.getName())) {
         buffer.append("?");
         sqlParamList.add(node.getName());
      }
      else {
         if (node.getModifier() != null)
            buffer.append(node.getModifier()).append(" ");

         buffer.append(node.getName());
      }


      return rightHand;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTDefinitionStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      data = node.jjtGetChild(1).jjtAccept(this, data);
      buffer.append(" ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(";");

      // store variable definition in the symbol table
      ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
      ASTPrimitiveType type = (ASTPrimitiveType) node.jjtGetChild(1);
      symbolMap.put(ident.getName(), type.getType());

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTExpressionStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(";");
      return data;
   }

   /**
    * Data parameter is a String which is treated as a java object implementing 
    * valueOf() method.
    * 
    * {@inheritDoc}
    */
   public Object visit(ASTLiteral node, Object data) {
      String name = node.getName();

      // get name in correct format for its datatype
      if (!name.equalsIgnoreCase("null") && data instanceof String) {
         String type = (String) data;
         if (type.equalsIgnoreCase("double")) {
            double tmp = Double.parseDouble(name);
            name = String.valueOf(tmp);
         }
      }

      if (sqlMode)
         name = name.replace("\"", "\\\"");
      else if (name.equals("current")) {
         name = "new DateTime()";
         data = "DateTime";
      }
      else if (name.equals("today")) {
         name = "new LocalDate().toDateTimeAtStartOfDay()";
         data = "DateTime";
      }
      else if (name.equals("null")) {
         // do nothing
      }

      buffer.append(name);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTorExpression node, Object data) {
      data = visitOOP(node, "||", data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTandExpression node, Object data) {
      if (sqlMode) {
         data = node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append("\"");
         nl.write();
         buffer.append("+ \" and ");
         data = node.jjtGetChild(1).jjtAccept(this, data);
      }
      else {
         data = visitOOP(node, "&&", data);
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTprepositionExpression node, Object data) {
      // in_res_source in "C", "N"
      // (in_res_source.equals("C") || in_res_source.equals("N"))
      if (node.getOperator().equals("in") || node.getOperator().equals("not in")) {

         if (sqlMode) {
            // child is a list
            if (node.jjtGetChild(1) instanceof ASTExpressionList) {
               data = node.jjtGetChild(0).jjtAccept(this, data);
               buffer.append(" ").append(node.getOperator()).append(" (");
               data = node.jjtGetChild(1).jjtAccept(this, data);
               buffer.append(")");
            }
            // child is a SQL statement
            else {
               data = node.jjtGetChild(0).jjtAccept(this, data);
               buffer.append(" ").append(node.getOperator()).append(" (\"");
               data = node.jjtGetChild(1).jjtAccept(this, data);
               nl.write();
               buffer.append("+ \")");
            }
         }
         else {
            buffer.append("(");

            if (node.jjtGetChild(1) instanceof ASTSelectExpression) {
               node.jjtGetChild(1).jjtAccept(this, null);
            }
            else {
               ASTExpressionList list = (ASTExpressionList) node.jjtGetChild(1);
               for (int idx = 0; idx < list.jjtGetNumChildren(); idx++) {
                  if (idx > 0) {
                     if (node.getOperator().equals("in"))
                        buffer.append(" || ");
                     else
                        buffer.append(" && ");
                  }

                  if (node.getOperator().equals("not in")) {
                     buffer.append("!");
                  }

                  node.jjtGetChild(0).jjtAccept(this, null);
                  buffer.append(".equals(");
                  list.jjtGetChild(idx).jjtAccept(this, null);
                  buffer.append(")");
               }
            }

            buffer.append(")");
         }
      }
      /*      else if (node.getOperator().equals("not")) {
               buff.append("(");
               
               ASTExpressionList list = (ASTExpressionList) node.jjtGetChild(1);
               for (int idx = 0; idx < list.jjtGetNumChildren(); idx++) {
                  if (idx > 0)
                     buff.append(" || ");
                  
                  buff.append("!");
                  data = node.jjtGetChild(0).jjtAccept(this, data);
                  buff.append(".equals(");
                  data = list.jjtGetChild(idx).jjtAccept(this, data);
                  buff.append(")");
               }
               
               buff.append(")");
            }
      */
      else if (node.getOperator().contains("exists")) {
         // TODO
         buffer.append("yyyyy");
      }
      else if (node.getOperator().equalsIgnoreCase("not exists")) {
         //startIndex = buffer.length()-1;
         buffer.append("XXXXXXXXXXXXX");
      }
      else if (node.getOperator().equals("between")) {
         if (sqlMode) {
            // TODO
         }
         else {
            ASTandExpression and = new ASTandExpression(0);
            and.jjtSetParent(node);
            
            ASTcomparisonExpression left = new ASTcomparisonExpression(0);
            left.jjtSetParent(and);
            left.setOperator(">=");
            left.jjtAddChild(node.jjtGetChild(0), 0);
            left.jjtAddChild(node.jjtGetChild(1), 1);

            ASTcomparisonExpression right = new ASTcomparisonExpression(0);
            right.jjtSetParent(and);
            right.setOperator("<=");
            right.jjtAddChild(node.jjtGetChild(0), 0);
            right.jjtAddChild(node.jjtGetChild(2), 1);
            
            and.jjtAddChild(left, 0);
            and.jjtAddChild(right, 1);

            data = and.jjtAccept(this, data);
         }
      }
      else if (node.getOperator().equals("not between")) {
         if (sqlMode) {
            // TODO
         }
         else {
            ASTorExpression or = new ASTorExpression(0);
            or.jjtSetParent(node);
            
            ASTcomparisonExpression left = new ASTcomparisonExpression(0);
            left.jjtSetParent(or);
            left.setOperator("<");
            left.jjtAddChild(node.jjtGetChild(0), 0);
            left.jjtAddChild(node.jjtGetChild(1), 1);

            ASTcomparisonExpression right = new ASTcomparisonExpression(0);
            right.jjtSetParent(or);
            right.setOperator(">");
            right.jjtAddChild(node.jjtGetChild(0), 0);
            right.jjtAddChild(node.jjtGetChild(2), 1);
            
            or.jjtAddChild(left, 0);
            or.jjtAddChild(right, 1);

            data = or.jjtAccept(this, data);
         }
      }
      else {
         data = visitOOP(node, node.getOperator(), data);
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTcomparisonExpression node, Object data) {
      visitSpecialToken(node, data);

      if (sqlMode) {
         data = node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(" ").append(node.getOperator()).append(" ");
         data = node.jjtGetChild(1).jjtAccept(this, data);
         return data;
      }
      if (node.jjtGetChild(0) instanceof ASTIdentifier) {
         ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
         data = symbolMap.get(ident.getName());
      }
      else if (node.jjtGetChild(0) instanceof ASTLiteral) {
         ASTLiteral lit = (ASTLiteral) node.jjtGetChild(0);
         if (lit.getName().equalsIgnoreCase("current") || lit.getName().equalsIgnoreCase("today"))
               data = "DateTime";
      }

      if (data instanceof String && ((String) data).equalsIgnoreCase("DateTime")) {
         if (node.getOperator().equals("<")) {
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isBefore(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(")");
         }
         else if (node.getOperator().equals("<=")) {
            buffer.append("(");
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isBefore(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(") || ");
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isEqual(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append("))");
         }
         else if (node.getOperator().equals("=")) {
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isEqual(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(")");
         }
         else if (node.getOperator().equals("<>") || node.getOperator().equals("!=")) {
            buffer.append("!");
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isEqual(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(")");
         }
         else if (node.getOperator().equals(">=")) {
            buffer.append("(");
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isAfter(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(") || ");
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isEqual(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append("))");
         }
         else if (node.getOperator().equals(">")) {
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".isAfter(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(")");
         }
         else if (node.getOperator().equals("is") && node.jjtGetChild(1) instanceof ASTLiteral) {
            ASTLiteral lit = (ASTLiteral) node.jjtGetChild(1);
            if (lit.getName().equalsIgnoreCase("null")) {
               node.jjtGetChild(0).jjtAccept(this, null);
               buffer.append(" == ");
               node.jjtGetChild(1).jjtAccept(this, null);
            }
            else {
               node.jjtGetChild(0).jjtAccept(this, null);
               buffer.append(".isEqual(");
               node.jjtGetChild(1).jjtAccept(this, null);
               buffer.append(")");
            }
         }
         else if (node.getOperator().equals("is not") && node.jjtGetChild(1) instanceof ASTLiteral) {
            ASTLiteral lit = (ASTLiteral) node.jjtGetChild(1);
            if (lit.getName().equalsIgnoreCase("null")) {
               node.jjtGetChild(0).jjtAccept(this, null);
               buffer.append(" != ");
               node.jjtGetChild(1).jjtAccept(this, null);
            }
            else {
               node.jjtGetChild(0).jjtAccept(this, null);
               buffer.append(".isEqual(");
               node.jjtGetChild(1).jjtAccept(this, null);
               buffer.append(")");
            }
         }
      }
      else if (node.getOperator().equals("=") || node.getOperator().equals("==")) {
         if (sqlMode) {
            data = visitOOP(node, "=", data);
         }
         else {
            node.jjtGetChild(0).jjtAccept(this, null);
            buffer.append(".equals(");
            node.jjtGetChild(1).jjtAccept(this, null);
            buffer.append(")");
         }
      }
      else if (node.getOperator().equals("!=")) {
         buffer.append("!");
         data = node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(".equals(");
         data = node.jjtGetChild(1).jjtAccept(this, data);
         buffer.append(")");
      }
      else if (node.getOperator().equals("is not") || node.getOperator().equals("<>")) {
         data = visitOOP(node, "!=", data);
      }
      else if (node.getOperator().equals("is")) {
         data = visitOOP(node, "==", data);
      }
      else {
         data = visitOOP(node, node.getOperator(), data);
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTnotExpression node, Object data) {
      buffer.append("not ");
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTquantitativeExpression node, Object data) {
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTconcatenationExpression node, Object data) {
      if (sqlMode) {
         data = visitOOP(node, "||", data);
      }
      else {
         data = visitOOP(node, "+", data);
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTadditiveExpression node, Object data) {
      // need to perform nest detection
      if (node.jjtGetChild(1) instanceof ASTunitsExpression) {
         if (node.getOperator().equals("+")) {
            node.jjtGetChild(0).jjtAccept(this, data);
            buffer.append(".plus");
            node.jjtGetChild(1).jjtAccept(this, data);
         }
         else if (node.getOperator().equals("-")) {
            node.jjtGetChild(0).jjtAccept(this, data);
            buffer.append(".minus");
            node.jjtGetChild(1).jjtAccept(this, data);
         }
      }
      else if (node.jjtGetChild(0) instanceof ASTIdentifier) {
         ASTIdentifier iden = (ASTIdentifier) node.jjtGetChild(0);
         String type = symbolMap.get(iden.getName());
         if (type != null && type.equalsIgnoreCase("DateTime")) {
            if (node.getOperator().equals("+")) {
               node.jjtGetChild(0).jjtAccept(this, data);
               buffer.append(".plusDays(");
               node.jjtGetChild(1).jjtAccept(this, data);
               buffer.append(")");
            }
            else if (node.getOperator().equals("-")) {
               node.jjtGetChild(0).jjtAccept(this, data);
               buffer.append(".minusDays(");
               node.jjtGetChild(1).jjtAccept(this, data);
               buffer.append(")");
            }
            // pass thru
            data = type;
         }
         else {
            data = visitOOP(node, node.getOperator(), data);
         }
      }
      else {
         data = visitOOP(node, node.getOperator(), data);
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTmultiplicativeExpression node, Object data) {
      data = visitOOP(node, node.getOperator(), data);
      return data;
   }

   public Object visitOOP(Node root, String oper, Object data) {
      if (explicitOOP)
         buffer.append("(");

      root.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(" ").append(oper).append(" ");
      root.jjtGetChild(1).jjtAccept(this, data);

      if (explicitOOP)
         buffer.append(")");

      return data;
   }

   public Object visitSpecialToken(SimpleNode root, Object data) {
      Token t = root.jjtGetFirstToken();

      if (t != null) {
         Token tt = t.specialToken;
         if (tt != null) {
            while (tt.specialToken != null)
               tt = tt.specialToken;
            while (tt != null) {
               if (tt.image.startsWith("--")) {
                  tt.image = tt.image.replaceFirst("--", "//");
               }
               else if (tt.image.startsWith("{") && tt.image.endsWith("}")) {
                  // delete parentheses
                  tt.image = tt.image.replaceFirst("\\{", "");
                  tt.image = tt.image.replaceAll("\\}", "");

                  // delete leading whitespace/newline
                  while (tt.image.startsWith("\r\n") || tt.image.startsWith(" ")) {
                     if (tt.image.startsWith("\r\n"))
                        tt.image = tt.image.substring(2);
                     else if (tt.image.startsWith(" "))
                        tt.image = tt.image.substring(1);
                  }
                  // delete trailing whitespace/newline
                  while (tt.image.endsWith("\r\n") || tt.image.endsWith(" ")) {
                     if (tt.image.endsWith("\r\n")) {
                        tt.image = tt.image.substring(0, tt.image.length() - 2);
                     }
                     else if (tt.image.endsWith(" ")) {
                        tt.image = tt.image.substring(0, tt.image.length() - 1);
                     }
                  }
                  // reformat into non-javadoc block comment
                  tt.image = tt.image.replaceAll("\\*/", "");
                  tt.image = tt.image.replaceAll("\r\n", "\r\n" + ws.indentString() + " * ");
                  tt.image = "/*\n"
                        .concat(ws.indentString())
                        .concat(" * ")
                        .concat(tt.image)
                        .concat("\n")
                        .concat(ws.indentString())
                        .concat(" */");
               }

               nl.write();
               buffer.append(tt.image);
               tt = tt.next;
            }
         }
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTunaryExpression node, Object data) {
      buffer.append(node.getOperator());
      data = node.jjtGetChild(0).jjtAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTunitsExpression node, Object data) {
      ASTintervalParam param = (ASTintervalParam) node.jjtGetChild(1);
      String unitType = param.getParam();
      if (unitType.equalsIgnoreCase("day")) {
         buffer.append("Days(");
         node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else if (unitType.equalsIgnoreCase("hour")) {
         buffer.append("Hours(");
         node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else if (unitType.equalsIgnoreCase("month")) {
         buffer.append("Months(");
         node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else if (unitType.equalsIgnoreCase("second")) {
         buffer.append("Seconds(");
         node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else if (unitType.equalsIgnoreCase("year")) {
         buffer.append("Years(");
         node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else {
         data = node.childrenAccept(this, data);
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTExpressionList node, Object data) {
      visitSpecialToken(node, data);

      ProcedureInformation procInfo = null;
      if (data != null && data instanceof ProcedureInformation)
         procInfo = (ProcedureInformation) data;

      if (node.jjtGetParent() instanceof ASTReturnStatement) {
         switch (node.jjtGetNumChildren()) {
         case 1:
            break;
         default:
            buffer.append("new ArrayList<Object>(Arrays.<Object>asList(");
            break;
         }
      }

      if (procInfo != null) {

         for (int idx = 0; idx < procInfo.getInputTypes().size(); idx++) {
            if (idx > 0)
               buffer.append(", ");

            if (idx < node.jjtGetNumChildren()) {
               if (procInfo.getInputTypes().get(idx).equalsIgnoreCase("DateTime")) {
                  node.jjtGetChild(idx).jjtAccept(this, null);
               }
               else if (autobox) {
                  buffer.append(procInfo.getInputTypes().get(idx));
                  buffer.append(".valueOf(");
                  node.jjtGetChild(idx).jjtAccept(this, null);
                  buffer.append(")");
               }
               else {
                  node.jjtGetChild(idx).jjtAccept(this, null);
               }
            }
            // If there aren't enough input arguments, pad with null
            else {
               buffer.append("null");
            }
         }
      }
      else {
         for (int idx = 0; idx < node.jjtGetNumChildren(); idx++) {
            if (idx > 0)
               buffer.append(", ");

            node.jjtGetChild(idx).jjtAccept(this, null);
         }
      }

      if (node.jjtGetParent() instanceof ASTReturnStatement) {
         switch (node.jjtGetNumChildren()) {
         case 1:
            break;
         default:
            buffer.append("))");
            break;
         }
      }

      return null;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTassignmentExpression node, Object data) {

      startIndex = buffer.length() - 1;

      ASTIdentifierList nameList = (ASTIdentifierList) node.jjtGetChild(0);
      ASTExpressionList expList = (ASTExpressionList) node.jjtGetChild(1);

      if (nameList.jjtGetNumChildren() > 1 && expList.jjtGetNumChildren() == 1
            && expList.jjtGetChild(0) instanceof ASTpostfixExpression) {
         buffer.append("Iterator<Object> ").append(variable("it")).append(" = ");
         data = expList.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(".iterator();");

         for (int i = 0; i < nameList.jjtGetNumChildren(); i++) {
            if (i > 0) {
               buffer.append(";");
            }

            nl.write();
            ASTIdentifier name = (ASTIdentifier) nameList.jjtGetChild(i);
            String type = symbolMap.get(name.getName());
            data = name.jjtAccept(this, type);
            buffer.append(" = (").append(type).append(") ").append(variable("it")).append(".next()");
         }
      }
      else if (nameList.jjtGetNumChildren() != expList.jjtGetNumChildren()) {
         data = expList.childrenAccept(this, data);
         logger.warn("Invalid semantics detected in assignmentExpression");
      }
      else {
         for (int i = 0; i < nameList.jjtGetNumChildren(); i++) {
            if (i > 0) {
               buffer.append(";");
            }

            ASTIdentifier name = (ASTIdentifier) nameList.jjtGetChild(i);
            String leftHandType = symbolMap.get(name.getName());
            data = name.jjtAccept(this, leftHandType);
            buffer.append(" = ");
            // pass the type to the other identifier
            data = expList.jjtGetChild(i).jjtAccept(this, leftHandType);
         }

      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTpostfixExpression node, Object data) {

      MethodInvocation mi = new MethodInvocation(this);

      if (!(node.jjtGetChild(0) instanceof ASTIdentifier)) {
         logger.fatal("Postfix expression without an identifier!");
      }

      // get method name
      ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
      mi.setName(ident.getName());

      // get procedure information
      ProcedureInformation procInfo = symbols.getProcedureInfo(ident.getName());
      Integer inputCount = null;
      Integer outputCount = null;
      if (procInfo != null) {
         mi.setMode(MethodInvocation.INVOKED);
         inputCount = procInfo.getInputTypes().size();
         outputCount = procInfo.getOutputTypes().size();
      }
      // if method name is a variable, dynamic call
      else if (procInfo == null && symbolMap.containsKey(ident.getName()) && !ident.getName().equalsIgnoreCase("count")) {
         mi.setMode(MethodInvocation.DYNAMIC);
      }

      // get input arguments
      ASTExpressionList input = (ASTExpressionList) node.jjtGetChild(1);
      if (inputCount == null)
         inputCount = input.jjtGetNumChildren();
      for (int i = 0; i < inputCount; i++) {
         String type = null;

         if (procInfo != null) {
            type = procInfo.getInputTypes().get(i);
         }
         else if (procInfo == null && symbolMap.containsKey(ident.getName())) {
            type = symbolMap.get(ident.getName());
         }

         Node n = null;
         if (input.jjtGetNumChildren() > i)
            n = input.jjtGetChild(i);
         else
            n = null;

         mi.addInputNode(n, type);
      }

      // get output arguments
      ASTIdentifierList output = null;
      if (node.jjtGetNumChildren() == 3) {
         Node returnvars = node.jjtGetChild(2);
         if (returnvars instanceof ASTIntoClause) {
            intoClause = (ASTIntoClause) returnvars;
            output = (ASTIdentifierList) returnvars.jjtGetChild(0);
         }
         else {
            output = (ASTIdentifierList) returnvars;
         }
         if (outputCount == null)
            outputCount = output.jjtGetNumChildren();
         for (int i = 0; i < outputCount; i++) {
            ASTIdentifier out = (ASTIdentifier) output.jjtGetChild(i);
            String type = null;

            if (procInfo != null) {
               List<String> types = procInfo.getOutputTypes();
               // ignore extra specified return arguments
               if (outputCount <= i)
                  break;
               type = types.get(i);
            }
            else if (symbolMap.get(out.getName()) != null) {
               type = symbolMap.get(out.getName());
            }

            mi.addOutputVariable(out.getName(), type);
         }
      }

      // write the method
      mi.write();

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTIdentifierList node, Object data) {
      data = visitList(node, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTReturnStatement node, Object data) {
      if (node.isResume()) {
         // TODO
      }

      visitSpecialToken(node, data);

      nl.write();
      buffer.append("return");
      switch (pi.getOutputTypes().size()) {
      case 0:
         break;
      case 1:
         buffer.append(" ");
         // TODO detect if return variable is correct type and autobox if not
         node.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, null);
         //node.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, pi.getOutputTypes().get(0));
         break;
      default:
         buffer.append(" new ArrayList<Object>(Arrays.<Object>asList(");
         for (int i = 0; i < pi.getOutputTypes().size(); i++) {
            if (i > 0)
               buffer.append(", ");

            if (node.jjtGetNumChildren() > 0) {
               ASTExpressionList list = (ASTExpressionList) node.jjtGetChild(0);
               if (list.jjtGetNumChildren() > i) {
                  if (autobox) {
                     buffer.append(pi.getOutputTypes().get(i)).append(".valueOf(");
                     list.jjtGetChild(i).jjtAccept(this, Boolean.TRUE);
                     buffer.append(")");
                  }
                  else {
                     list.jjtGetChild(i).jjtAccept(this, null);
                  }
               }
               else {
                  buffer.append("null");
               }
            }
            else {
               buffer.append("null");
            }
         }
         buffer.append("))");
         break;
      }
      buffer.append(";");
      return null;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTIfClause node, Object data) {
      visitSpecialToken(node, data);
      nl.write();

      buffer.append("if (");
      node.jjtGetChild(0).jjtAccept(this, null);
      buffer.append(") {");
      ws.incLevel();
      node.jjtGetChild(1).jjtAccept(this, null);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTElifClause node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("else if (");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(") {");
      ws.incLevel();
      data = node.jjtGetChild(1).jjtAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTElseClause node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("else {");
      ws.incLevel();
      data = node.jjtGetChild(0).jjtAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTWhileStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("while (");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(") {");
      ws.incLevel();
      data = node.jjtGetChild(1).jjtAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTContinueLoopStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("continue;");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTExitLoopStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("break;");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTRaiseExceptionStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("throw new ProcedureException(");

      int pos = 0;
      switch (node.jjtGetNumChildren()) {
      case 3:
         node.jjtGetChild(pos++).jjtAccept(this, null);
         buffer.append(", ");
      case 2:
         node.jjtGetChild(pos++).jjtAccept(this, null);
         buffer.append(", ");
      case 1:
         node.jjtGetChild(pos++).jjtAccept(this, null);
         break;
      }

      buffer.append(");");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSQLStatement node, Object data) {
      visitSpecialToken(node, data);
      data = node.childrenAccept(this, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTdropTableExpression node, Object data) {
      ASTIdentifier table = (ASTIdentifier) node.jjtGetChild(0);
      openStatement();
      buffer.append(" = prepareStatement(\"drop table ")
            .append(table.getName())
            .append("\");");
      nl.write();
      buffer.append("executeUpdate(")
            .append(variable("pstmt"))
            .append(");");
      closeStatement();
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSelectExpression node, Object data) {
      // Handle UNION case.
      if (node.jjtGetParent() instanceof ASTSelectExpression) {
         nl.write();
         buffer.append("+ \" union \"");
      }
      else if (inString || node.jjtGetParent() instanceof ASTprepositionExpression) {
         //buffer.append("(\"");
         ws.incLevel();
      }
      // if we aren't in sql mode, then set everything up
      //if (sqlMode == false) {
      else {
         sqlSymbolMap.clear();
         sqlParamList.clear();
         sqlMode = true;
         openStatement();
         buffer.append(" = prepareStatement(");
         ws.incLevel();
         ws.incLevel();
      }

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {

         Node clause = node.jjtGetChild(i);

         if (clause instanceof ASTIntoClause) {
            intoClause = (ASTIntoClause) clause;
            intoClause.setResultSet(true);
         }
         else {
            data = clause.jjtAccept(this, data);
         }
      }

      if (!(node.jjtGetParent() instanceof ASTNestedExpression) && !(node.jjtGetParent() instanceof ASTprepositionExpression)) {
         /*         if (hasParent(node, ASTSelectExpression.class)) {
                     nl.write();
                     buffer.append("\")");
                  }
         */ws.decLevel();
         ws.decLevel();
         sqlMode = false;
         inString = false;
         buffer.append(");");
         nl.write();

         // set '?' sql parameters
         data = writeSqlParams(data);

         // execute query
         openResultSet();
      }

      // this needs to happen later in the for-each case
      if (node.jjtGetParent() instanceof ASTSQLStatement) {
         // move cursor to 1st row
         nl.write();
         buffer.append(variable("rs")).append(".next();");

         // process INTO clause
         if (intoClause != null) {
            data = intoClause.jjtAccept(this, data);
         }
         closeStatement();
         closeResultSet();
      }

      return data;
   }

   String getGetter(String type) {
      String getter = "getObject";
      if (type != null) {
         if (type.equals("String")) {
            getter = "getString";
         }
         else if (type.equalsIgnoreCase("Integer")) {
            getter = "getInt";
         }
         else if (type.equalsIgnoreCase("Date")) {
            getter = "getDate";
         }
         else if (type.equalsIgnoreCase("DateTime")) {
            getter = "getTimestamp";
         }
         else if (type.equalsIgnoreCase("Double")) {
            getter = "getDouble";
         }
         else if (type.equalsIgnoreCase("Boolean")) {
            getter = "getBoolean";
         }
      }
      return getter;
   }

   // datetime should use setObject, JDBC engine knows what to do w/ java.sql.*
   String getSetter(String type) {
      String setter = "setObject";
      if (type != null) {
         if (type.equalsIgnoreCase("Integer")) {
            setter = "setInt";
         }
         else if (type.equalsIgnoreCase("String")) {
            setter = "setString";
         }
         else if (type.equalsIgnoreCase("Date")) {
            setter = "setDate";
         }
         else if (type.equalsIgnoreCase("Double")) {
            setter = "setDouble";
         }
         else if (type.equalsIgnoreCase("Boolean")) {
            setter = "setBoolean";
         }
      }
      return setter;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTAssignmentStatementList node, Object data) {
      // TODO Auto-generated method stub
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTAssignmentStatement node, Object data) {
      startIndex = buffer.length() - 1;

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTTraceStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("trace(\"");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append("\");");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSetDebugFileStatement node, Object data) {
      visitSpecialToken(node, data);
      nl.write();
      buffer.append("setDebugFile(");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(");");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTTraceArgument node, Object data) {
      buffer.append(node.getName());
      return data;
   }

   /**
    * Note: Nested exceptions are not currently supported by this strategy.
    * <p>
    * {@inheritDoc}
    */
   public Object visit(ASTOnExceptionStatement node, Object data) {
      visitSpecialToken(node, data);
      // if node isn't opened
      if (!node.isOpen()) {
         nl.write();
         buffer.append("try {");
         node.setOpen(true);
         Node parent = node.jjtGetParent();
         // add self to the end to be reprocessed
         parent.jjtAddChild(node, parent.jjtGetNumChildren());
         ws.incLevel();
      }
      else {
         ws.decLevel();
         nl.write();
         buffer.append("}");
         nl.write();
         buffer.append("catch (SQLException e) {");
         ws.incLevel();
         nl.write();
         buffer.append("sql_error = e.getErrorCode();");
         nl.write();
         buffer.append("isam_error = 0;");
         nl.write();
         buffer.append("error_data = e.getMessage();");
         // exception children
         data = node.jjtGetChild(1).jjtAccept(this, data);
         ws.decLevel();
         nl.write();
         buffer.append("}");
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTNestedExpression node, Object data) {

      // HACK nested select exp. must be calculated first
      if (!sqlMode && node.jjtGetChild(0) instanceof ASTSelectExpression) {
         String tmp = buffer.substring(startIndex + 1);
         buffer.delete(startIndex, buffer.length() - 1);
         //inString = false;
         node.childrenAccept(this, null);
         nl.write();
         buffer.append((String) data).append(" ").append(variable("tmp", true)).append(" = ");
         if (((String) data).equalsIgnoreCase("datetime")) {
            buffer.append("new DateTime(")
                  .append(variable("rs"))
                  .append(".")
                  .append(getGetter((String) data))
                  .append("(1).getTime());");
         }
         else {
            buffer.append(variable("rs")).append(".").append(getGetter((String) data)).append("(1);");
         }
         nl.write();
         buffer.append(tmp).append(variable("tmp")).append("");
      }
      else {
         data = node.childrenAccept(this, data);
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTTableDeclList node, Object data) {
      data = visitList(node, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTTableDecl node, Object data) {
      switch (node.jjtGetNumChildren()) {
      case 1:
         ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
         sqlSymbolMap.put(ident.getName(), ident.getName());
         buffer.append(ident.getName());
         break;
      case 2:
         ASTIdentifier ident1 = (ASTIdentifier) node.jjtGetChild(0);
         ASTIdentifier ident2 = (ASTIdentifier) node.jjtGetChild(1);
         sqlSymbolMap.put(ident1.getName(), ident2.getName());
         buffer.append(ident1.getName()).append(" ").append(ident2.getName());
         break;
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTCreateTableStatement node, Object data) {
      // TODO create table
      if (node.getModifier() != null) {

      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTOrderByExpressionList node, Object data) {
      data = visitList(node, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTOrderByExpression node, Object data) {
      data = node.childrenAccept(this, data);
      if (node.getModifier() != null)
         buffer.append(" ").append(node.getModifier());
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTFromExpressionList node, Object data) {
      data = visitList(node, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTFromExpression node, Object data) {

      // first check if the from expression is a nested sql expression
      if (node.jjtGetChild(0) instanceof ASTSelectExpression) {
         buffer.append("(\"");
         node.jjtGetChild(0).jjtAccept(this, null);
         nl.write();
         buffer.append("+ \")");
      }
      else if (node.isOuter()) {
         buffer.append(" outer (");
         data = node.jjtGetChild(0).jjtAccept(this, data);
         buffer.append(")");
      }
      else {
         // table declaration
         data = node.jjtGetChild(0).jjtAccept(this, data);

         if (node.isInner() || node.isOuter()) {
            buffer.append("\"");
            nl.write();
            buffer.append("+ \" ");

            if (node.isInner()) {
               for (int i = 1; i < node.jjtGetNumChildren(); i += 2) {
                  buffer.append(" inner join ");
                  data = node.jjtGetChild(i).jjtAccept(this, data);
                  buffer.append(" on ");
                  data = node.jjtGetChild(i + 1).jjtAccept(this, data);
               }
            }
            else {
            }
         }
      }

      return null;
   }

   public Object visitList(Node node, Object data) {
      return visitList(node, data, false);
   }

   public Object visitList(Node node, Object data, boolean omitComma) {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         if (i > 0) {
            if (!omitComma)
               buffer.append(",");
            buffer.append(" ");
         }
         data = node.jjtGetChild(i).jjtAccept(this, data);
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTForEachStatement node, Object data) {
      visitSpecialToken(node, data);

      if (node.isHoldable()) {
         nl.write();
         buffer.append("setHoldability(java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT);");
      }
      data = node.jjtGetChild(0).jjtAccept(this, data);

      if (node.jjtGetChild(0) instanceof ASTSelectExpression) {
         nl.write();
         buffer.append("while (").append(variable("rs")).append(".next()) {");
      }
      else {
         nl.write();
         buffer.append("while (").append(variable("it")).append(".hasNext()) {");
      }

      ws.incLevel();

      // Kind of painful to read, but the 'into' clause needs to be translated inside the while loop
      if (intoClause != null)
         data = intoClause.jjtAccept(this, data);

      // visit foreach statements
      data = node.jjtGetChild(1).jjtAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
            if (node.jjtGetChild(0) instanceof ASTSelectExpression) {
               closeStatement();
               closeResultSet();
            }
      if (node.isHoldable()) {
         nl.write();
         buffer.append("setHoldability(java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT);");
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTAggregateFunction node, Object data) {
      buffer.append(node.getName());
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTUpdateExpression node, Object data) {
      sqlSymbolMap.clear();
      sqlParamList.clear();

      openStatement();
      buffer.append(" = prepareStatement(");
      ws.incLevel();
      ws.incLevel();
      sqlMode = true;

      // write clauses
      data = node.childrenAccept(this, data);

      ws.decLevel();
      ws.decLevel();
      buffer.append(");");
      nl.write();

      // set '?' sql parameters
      data = writeSqlParams(data);

      // execute update
      nl.write();
      buffer.append("executeUpdate(").append(variable("pstmt")).append(");");
      closeStatement();
      sqlMode = false;

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTDeleteExpression node, Object data) {
      sqlSymbolMap.clear();
      sqlParamList.clear();

      openStatement();
      buffer.append(" = prepareStatement(");
      ws.incLevel();
      ws.incLevel();

      // write delete clause
      data = node.jjtGetChild(0).jjtAccept(this, data);
      sqlMode = true;
      data = node.jjtGetChild(1).jjtAccept(this, data);

      ws.decLevel();
      ws.decLevel();
      buffer.append(");");

      // set '?' sql parameters
      data = writeSqlParams(data);

      // execute update
      nl.write();
      buffer.append("executeUpdate(").append(variable("pstmt")).append(");");
      closeStatement();
      sqlMode = false;

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTInsertExpression node, Object data) {
      sqlSymbolMap.clear();
      sqlParamList.clear();

      if (!(node.jjtGetParent() instanceof ASTNestedExpression)) {
         openStatement();
         buffer.append(" = prepareInsert(");
         ws.incLevel();
         ws.incLevel();
      }
      else {
         ws.incLevel();
      }

      // write insert clause
      data = node.jjtGetChild(0).jjtAccept(this, data);
      sqlMode = true;
      data = node.jjtGetChild(1).jjtAccept(this, data);

      if (!(node.jjtGetParent() instanceof ASTNestedExpression)) {
         ws.decLevel();
         ws.decLevel();
         buffer.append(");");

         // set '?' sql parameters
         data = writeSqlParams(data);

         // execute update
         nl.write();
         buffer.append("executeUpdate(").append(variable("pstmt")).append(");");
         closeStatement();
         sqlMode = false;
      }
      else {
         ws.decLevel();
      }

      return data;
   }

   public Object writeSqlParams(Object data) {
      for (Integer i = 1; i <= sqlParamList.size(); i++) {
         String name = sqlParamList.get(i - 1);
         String type = symbolMap.get(name);
         String setter = getSetter(type);

         // must do a null check on java object
         nl.write();
         buffer.append("if (").append(name).append(" != null) {");
         ws.incLevel();
         nl.write();
         buffer.append(variable("pstmt"))
               .append(".")
               .append(setter)
               .append("(")
               .append(i.toString())
               .append(", ")
               .append(name)
               .append(");");
         ws.decLevel();
         nl.write();
         buffer.append("}");
         nl.write();
         buffer.append("else {");
         ws.incLevel();
         nl.write();
         buffer.append(variable("pstmt")).append(".setNull(").append(i.toString()).append(", Types.JAVA_OBJECT);");
         ws.decLevel();
         nl.write();
         buffer.append("}");
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTTransactionStatement node, Object data) {
      visitSpecialToken(node, data);

      nl.write();
      if (node.getCommand().equals("begin")) {
         buffer.append("begin();");
      }
      else if (node.getCommand().equals("commit")) {
         buffer.append("commit();");
      }
      else if (node.getCommand().equals("rollback")) {
         buffer.append("rollback();");
      }
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTintervalParams node, Object data) {
      data = visitOOP(node, "to", data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTintervalParam node, Object data) {
      // handled upstream
      return null;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTInsertClause node, Object data) {
      nl.write();
      buffer.append("  \"insert into ");
      node.jjtGetChild(0).jjtAccept(this, null);

      if (node.jjtGetNumChildren() > 1) {
         buffer.append(" (");
         node.jjtGetChild(1).jjtAccept(this, null);
         buffer.append(")");
      }
      buffer.append("\"");

      return null;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSelectClause node, Object data) {

      Node par = node.jjtGetParent().jjtGetParent();
      if (inString || par instanceof ASTSelectExpression || par instanceof ASTprepositionExpression
            || par instanceof ASTFromExpression) {
         nl.write();
         buffer.append("+ \"select ");
      }
      else {
         nl.write();
         buffer.append("  \"select ");
         inString = true;
      }

      data = node.childrenAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTUpdateClause node, Object data) {
      nl.write();
      buffer.append("  \"update ");
      data = node.childrenAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTFromClause node, Object data) {
      nl.write();
      buffer.append("+ \" from ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTIntoClause node, Object data) {
      ASTIdentifierList list = (ASTIdentifierList) node.jjtGetChild(0);
      for (Integer i = 1; i <= list.jjtGetNumChildren(); i++) {
         String name = ((ASTIdentifier) list.jjtGetChild(i - 1)).getName();
         String type = symbolMap.containsKey(name) ? symbolMap.get(name) : "Object";

         if (node.isResultSet()) {
            String getter = getGetter(type);

            nl.write();
            buffer.append(name).append(" = ");
            if (type.equalsIgnoreCase("datetime")) {
               buffer.append("new DateTime(")
                     .append(variable("rs"))
                     .append(".")
                     .append(getter)
                     .append("(")
                     .append(i.toString())
                     .append(").getTime());");
            }
            else {
               buffer.append(variable("rs")).append(".").append(getter).append("(").append(i.toString()).append(");");
            }

         }
         else {
            nl.write();
            buffer.append(name).append(" = (").append(type).append(") ").append(variable("it")).append(".next();");
         }

      }
      // delete self after being visited
      intoClause = null;
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTValuesClause node, Object data) {
      nl.write();
      buffer.append("+ \" values (");
      data = node.childrenAccept(this, data);
      buffer.append(")\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSetClause node, Object data) {
      nl.write();
      buffer.append("+ \" set ");
      data = node.childrenAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTWhereClause node, Object data) {
      nl.write();
      buffer.append("+ \" where ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      if (!(node.jjtGetParent().jjtGetParent() instanceof ASTNestedExpression))
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTGroupByClause node, Object data) {
      nl.write();
      buffer.append("+ \" group by ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTOrderByClause node, Object data) {
      nl.write();
      buffer.append("+ \" order by ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTBlockStatement node, Object data) {
      nl.write();
      buffer.append("{");
      ws.incLevel();
      data = node.childrenAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTsubstringExpression node, Object data) {
      // we can get away with the java method for this one
      buffer.append("substring(");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(", ");
      data = node.jjtGetChild(1).jjtAccept(this, data);
      buffer.append(", ");
      data = node.jjtGetChild(2).jjtAccept(this, data);
      buffer.append(")");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTLockTableExpression node, Object data) {
      // TODO lock mode statement
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTForStatement node, Object data) {
      //    "for" Identifier() "=" Expression() "to" Expression() CompoundExpression() "end" "for"
      nl.write();
      buffer.append("for (");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(" = ");
      data = node.jjtGetChild(1).jjtAccept(this, data);
      buffer.append("; ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append(" <= ");
      data = node.jjtGetChild(2).jjtAccept(this, data);
      buffer.append("; ");
      data = node.jjtGetChild(0).jjtAccept(this, data);
      buffer.append("++) {");
      ws.incLevel();
      data = node.jjtGetChild(3).jjtAccept(this, data);
      ws.decLevel();
      nl.write();
      buffer.append("}");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSetExpressionList node, Object data) {
      data = visitList(node, data);
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTSetExpression node, Object data) {
      // store the sql mode
      boolean tmp = sqlMode;

      if (node.jjtGetChild(0) instanceof ASTIdentifier) {
         {
            sqlMode = false;
            data = node.jjtGetChild(0).jjtAccept(this, data);
            sqlMode = tmp;
         }
         buffer.append(" = ");
         data = node.jjtGetChild(1).jjtAccept(this, data);
      }
      else {
         int numIdent = node.jjtGetChild(0).jjtGetNumChildren();
         int numExpress = node.jjtGetChild(1).jjtGetNumChildren();
         // Let's do a quick semantic check on arg counts
         if (numIdent != numExpress)
            logger.warn("Semantic error detected in SQL Update clause");

         buffer.append("(");
         {
            sqlMode = false;
            data = node.jjtGetChild(0).jjtAccept(this, data);
            sqlMode = tmp;
         }
         buffer.append(") = (");
         data = node.jjtGetChild(1).jjtAccept(this, data);
         buffer.append(")");
      }

      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTDeleteClause node, Object data) {
      nl.write();
      buffer.append("  \"delete from ");
      data = node.childrenAccept(this, data);
      buffer.append("\"");
      return data;
   }

   /**
    * {@inheritDoc}
    */
   public Object visit(ASTRevokeDeclaration node, Object data) {
      // intentionally don't process children
      return null;
   }

}
