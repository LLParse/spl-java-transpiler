/* Generated By:JJTree: Do not edit this line. ASTElseClause.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.dontexist.morals.splparser.node;

import com.dontexist.morals.splparser.*;

public
class ASTElseClause extends SimpleNode {
  public ASTElseClause(int id) {
    super(id);
  }

  public ASTElseClause(SPLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=08b08057999b0022aec18b2f1ea46443 (do not edit this line) */
