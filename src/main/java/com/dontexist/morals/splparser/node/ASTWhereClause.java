/* Generated By:JJTree: Do not edit this line. ASTWhereClause.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.dontexist.morals.splparser.node;

import com.dontexist.morals.splparser.*;

public
class ASTWhereClause extends SimpleNode {
  public ASTWhereClause(int id) {
    super(id);
  }

  public ASTWhereClause(SPLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9c59118e7c077554f3064f5b79b37fcb (do not edit this line) */
