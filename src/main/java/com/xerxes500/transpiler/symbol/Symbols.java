package com.xerxes500.transpiler.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James Oliver
 * @version $Id$
 */
public class Symbols {

   // map of procedure names to their information
   Map<String, ProcedureInformation> pi          = new HashMap<String, ProcedureInformation>();

   // variables used during symbol table creation
   private ProcedureInformation      curProc     = null;
   private List<String>              inputTypes  = new ArrayList<String>();
   private List<String>              outputTypes = new ArrayList<String>();

   public void nextProcedure(String name) {
      inputTypes = new ArrayList<String>();
      outputTypes = new ArrayList<String>();
      curProc = new ProcedureInformation(name, inputTypes, outputTypes);
      pi.put(name, curProc);
   }

   public void addProcedureInputType(String type) {
      inputTypes.add(type);
   }

   public void addProcedureOutputType(String type) {
      outputTypes.add(type);
   }

   public ProcedureInformation getProcedureInfo(String name) {
      return pi.get(name);
   }

   /**
    * Procedure information POJO.
    * 
    * @author James Oliver
    * @version $Id$
    */
   public class ProcedureInformation {
      private String       procedureName;
      private List<String> inputTypes   = new ArrayList<String>();
      private List<String> outputTypes  = new ArrayList<String>();
      private boolean      sqlException = false;

      public ProcedureInformation(String name, List<String> inputTypes, List<String> outputTypes) {
         this.procedureName = name;
         this.inputTypes = inputTypes;
         this.outputTypes = outputTypes;
      }

      public String getProcedureName() {
         return procedureName;
      }

      public List<String> getInputTypes() {
         return inputTypes;
      }

      public List<String> getOutputTypes() {
         return outputTypes;
      }

      public void setSQLException(boolean sqlException) {
         this.sqlException = sqlException;
      }

      public boolean throwsSQLException() {
         return sqlException;
      }
   }
}
