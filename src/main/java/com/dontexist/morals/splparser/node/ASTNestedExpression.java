/* Generated By:JJTree: Do not edit this line. ASTNestedExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.dontexist.morals.splparser.node;

import com.dontexist.morals.splparser.*;

public
class ASTNestedExpression extends SimpleNode {
  public ASTNestedExpression(int id) {
    super(id);
  }

  public ASTNestedExpression(SPLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=04afceedaed2b662a7c2ff1a57f2f721 (do not edit this line) */
