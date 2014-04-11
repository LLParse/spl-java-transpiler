package com.xerxes500.transpiler.symbol;

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
import com.xerxes500.transpiler.splparser.node.SPLParserVisitor;
import com.xerxes500.transpiler.splparser.node.SimpleNode;

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
