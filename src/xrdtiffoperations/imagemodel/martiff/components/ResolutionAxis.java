package xrdtiffoperations.imagemodel.martiff.components;

public class ResolutionAxis {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private int denominator;
    private int numerator;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public int getDenominator(){
        return denominator;
    }

    public int getNumerator(){
        return numerator;
    }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ResolutionAxis(int _num, int _denum){
        denominator = _denum;
        numerator = _num;
    }

}
