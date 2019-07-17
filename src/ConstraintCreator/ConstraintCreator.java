package ConstraintCreator;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.EntryLabel;
import Constraint.Term.EntryUnionAE;
import Constraint.Term.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ConstraintCreator {

    private ConstraintTermFactory variableFactory;
    private ASTNode prev;

    public ConstraintCreator() {
        variableFactory = new ConstraintTermFactory();
    }


}
