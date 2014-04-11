package com.dontexist.morals.symbol;

import com.dontexist.morals.splparser.node.ASTAggregateFunction;
import com.dontexist.morals.splparser.node.ASTArgument;
import com.dontexist.morals.splparser.node.ASTArgumentList;
import com.dontexist.morals.splparser.node.ASTAssignmentStatement;
import com.dontexist.morals.splparser.node.ASTAssignmentStatementList;
import com.dontexist.morals.splparser.node.ASTBlockStatement;
import com.dontexist.morals.splparser.node.ASTCompilationUnit;
import com.dontexist.morals.splparser.node.ASTCompoundStatement;
import com.dontexist.morals.splparser.node.ASTContinueLoopStatement;
import com.dontexist.morals.splparser.node.ASTCreateTableStatement;
import com.dontexist.morals.splparser.node.ASTDeclaration;
import com.dontexist.morals.splparser.node.ASTDefinitionStatement;
import com.dontexist.morals.splparser.node.ASTDeleteClause;
import com.dontexist.morals.splparser.node.ASTDeleteExpression;
import com.dontexist.morals.splparser.node.ASTElifClause;
import com.dontexist.morals.splparser.node.ASTElseClause;
import com.dontexist.morals.splparser.node.ASTExitLoopStatement;
import com.dontexist.morals.splparser.node.ASTExpressionList;
import com.dontexist.morals.splparser.node.ASTExpressionStatement;
import com.dontexist.morals.splparser.node.ASTForEachStatement;
import com.dontexist.morals.splparser.node.ASTForStatement;
import com.dontexist.morals.splparser.node.ASTFromClause;
import com.dontexist.morals.splparser.node.ASTFromExpression;
import com.dontexist.morals.splparser.node.ASTFromExpressionList;
import com.dontexist.morals.splparser.node.ASTGroupByClause;
import com.dontexist.morals.splparser.node.ASTIdentifier;
import com.dontexist.morals.splparser.node.ASTIdentifierList;
import com.dontexist.morals.splparser.node.ASTIfClause;
import com.dontexist.morals.splparser.node.ASTInsertClause;
import com.dontexist.morals.splparser.node.ASTInsertExpression;
import com.dontexist.morals.splparser.node.ASTIntoClause;
import com.dontexist.morals.splparser.node.ASTLiteral;
import com.dontexist.morals.splparser.node.ASTLockTableExpression;
import com.dontexist.morals.splparser.node.ASTNestedExpression;
import com.dontexist.morals.splparser.node.ASTOnExceptionStatement;
import com.dontexist.morals.splparser.node.ASTOrderByClause;
import com.dontexist.morals.splparser.node.ASTOrderByExpression;
import com.dontexist.morals.splparser.node.ASTOrderByExpressionList;
import com.dontexist.morals.splparser.node.ASTPrimitiveType;
import com.dontexist.morals.splparser.node.ASTPrimitiveTypeList;
import com.dontexist.morals.splparser.node.ASTProcedureConclusion;
import com.dontexist.morals.splparser.node.ASTProcedureDeclaration;
import com.dontexist.morals.splparser.node.ASTRaiseExceptionStatement;
import com.dontexist.morals.splparser.node.ASTReturnStatement;
import com.dontexist.morals.splparser.node.ASTRevokeDeclaration;
import com.dontexist.morals.splparser.node.ASTSQLStatement;
import com.dontexist.morals.splparser.node.ASTSelectClause;
import com.dontexist.morals.splparser.node.ASTSelectExpression;
import com.dontexist.morals.splparser.node.ASTSetClause;
import com.dontexist.morals.splparser.node.ASTSetDebugFileStatement;
import com.dontexist.morals.splparser.node.ASTSetExpression;
import com.dontexist.morals.splparser.node.ASTSetExpressionList;
import com.dontexist.morals.splparser.node.ASTTableDecl;
import com.dontexist.morals.splparser.node.ASTTableDeclList;
import com.dontexist.morals.splparser.node.ASTTraceArgument;
import com.dontexist.morals.splparser.node.ASTTraceStatement;
import com.dontexist.morals.splparser.node.ASTTransactionStatement;
import com.dontexist.morals.splparser.node.ASTUpdateClause;
import com.dontexist.morals.splparser.node.ASTUpdateExpression;
import com.dontexist.morals.splparser.node.ASTValuesClause;
import com.dontexist.morals.splparser.node.ASTWhereClause;
import com.dontexist.morals.splparser.node.ASTWhileStatement;
import com.dontexist.morals.splparser.node.ASTadditiveExpression;
import com.dontexist.morals.splparser.node.ASTandExpression;
import com.dontexist.morals.splparser.node.ASTassignmentExpression;
import com.dontexist.morals.splparser.node.ASTcomparisonExpression;
import com.dontexist.morals.splparser.node.ASTconcatenationExpression;
import com.dontexist.morals.splparser.node.ASTdropTableExpression;
import com.dontexist.morals.splparser.node.ASTintervalParam;
import com.dontexist.morals.splparser.node.ASTintervalParams;
import com.dontexist.morals.splparser.node.ASTmultiplicativeExpression;
import com.dontexist.morals.splparser.node.ASTnotExpression;
import com.dontexist.morals.splparser.node.ASTorExpression;
import com.dontexist.morals.splparser.node.ASTpostfixExpression;
import com.dontexist.morals.splparser.node.ASTprepositionExpression;
import com.dontexist.morals.splparser.node.ASTquantitativeExpression;
import com.dontexist.morals.splparser.node.ASTsubstringExpression;
import com.dontexist.morals.splparser.node.ASTunaryExpression;
import com.dontexist.morals.splparser.node.ASTunitsExpression;
import com.dontexist.morals.splparser.node.SPLParserVisitor;
import com.dontexist.morals.splparser.node.SimpleNode;

/**
 * This visitor is the first stage in a two-stage translation process. It gathers information
 * about procedure declarations so correct decisions may be made when procedure calls are
 * encountered before the definition of said procedure.
 * <p>
 * We are interested in 2 things specifically:
 * <ul>
 * <li>1) Procedure input argument datatypes</li>
 * <li>2) Procedure return argument datatypes</li>
 * </ul>
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class SymbolVisitor implements SPLParserVisitor {

   Symbols symbols = null;

   /**
    * {@inheritDoc}
    */

   public Object visit(SimpleNode node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTCompilationUnit node, Object data) {
      if (data == null)
         symbols = new Symbols();
      else
         symbols = (Symbols) data;

      node.childrenAccept(this, null);

      return symbols;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTDeclaration node, Object data) {
      node.childrenAccept(this, null);
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTProcedureDeclaration node, Object data) {
      symbols.nextProcedure(node.getName());
      node.childrenAccept(this, null);

      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTProcedureConclusion node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTArgumentList node, Object data) {
      // visit each Argument in the ArgumentList
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         symbols.addProcedureInputType((String) node.jjtGetChild(i).jjtAccept(this, null));
      }

      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTArgument node, Object data) {
      // return the argument datatype, ignore argument identifier
      return node.jjtGetChild(1).jjtAccept(this, null);
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTPrimitiveTypeList node, Object data) {
      // The list applies to output arguments based on syntax
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         symbols.addProcedureOutputType((String) node.jjtGetChild(i).jjtAccept(this, null));
      }
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTPrimitiveType node, Object data) {
      // return primitive type to parent
      return node.getType();
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTintervalParams node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTintervalParam node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTIdentifier node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTIdentifierList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTCompoundStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTBlockStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTDefinitionStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTExpressionStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTIfClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTElifClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTElseClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTForStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTForEachStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTWhileStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTContinueLoopStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTExitLoopStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTTraceStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTTraceArgument node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSetDebugFileStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTReturnStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTOnExceptionStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTRaiseExceptionStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTTransactionStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTExpressionList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTassignmentExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTorExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTandExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTprepositionExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTcomparisonExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTnotExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTquantitativeExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTconcatenationExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTadditiveExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTmultiplicativeExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTunaryExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTpostfixExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTunitsExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTsubstringExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTNestedExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTLiteral node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSQLStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTLockTableExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTdropTableExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTCreateTableStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTAggregateFunction node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTInsertExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSelectExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTUpdateExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTDeleteExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTDeleteClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTInsertClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSelectClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTUpdateClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTFromClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTIntoClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTValuesClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSetClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSetExpressionList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTSetExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTWhereClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTGroupByClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTOrderByClause node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTFromExpressionList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTFromExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTOrderByExpressionList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTOrderByExpression node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTTableDeclList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTTableDecl node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTAssignmentStatementList node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTAssignmentStatement node, Object data) {
      return null;
   }

   /**
    * {@inheritDoc}
    */

   public Object visit(ASTRevokeDeclaration node, Object data) {
      return null;
   }

}
