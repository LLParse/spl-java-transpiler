/* Generated By:JJTree: Do not edit this line. ASTconcatenationExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.dontexist.morals.splparser.node;

import com.dontexist.morals.splparser.*;

public
class ASTconcatenationExpression extends SimpleNode {
  public ASTconcatenationExpression(int id) {
    super(id);
  }

  public ASTconcatenationExpression(SPLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=38a4226f99b3184cf6a814481ddae309 (do not edit this line) */
