/* Generated By:JJTree: Do not edit this line. ASTTraceArgument.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.dontexist.morals.splparser.node;

import com.dontexist.morals.splparser.*;

public class ASTTraceArgument extends SimpleNode {
   String name;

   public ASTTraceArgument(int id) {
      super(id);
   }

   public ASTTraceArgument(SPLParser p, int id) {
      super(p, id);
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   /** Accept the visitor. **/
   public Object jjtAccept(SPLParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
/* JavaCC - OriginalChecksum=f492d2d60d2d3feee7c55c916aa4edc3 (do not edit this line) */
