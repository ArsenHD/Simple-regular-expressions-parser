import java.io.InputStream;
import java.text.ParseException;

class Parser {

    private LexicalAnalyzer lex;

    Tree parse(InputStream input) throws ParseException {
        lex = new LexicalAnalyzer(input);
        lex.nextToken();
        Tree regex = R();
        if (lex.getCurToken() != Token.END) {
            throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
        return regex;
    }

    private Tree R() throws ParseException {
        switch (lex.getCurToken()) {
            case CHAR:
            case LBRACKET:
                Tree concat = Concat();
                Tree rPrime = RPrime();
                return new Tree("R", concat, rPrime);
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree  RPrime() throws ParseException {
        switch (lex.getCurToken()) {
            case CHAR:
            case LBRACKET:
                Tree concat = Concat();
                Tree rPrime = RPrime();
                return new Tree("R'", concat, rPrime);
            case RBRACKET:
            case END:
                return new Tree("R'", new Tree("eps"));
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree Concat() throws ParseException {
        switch (lex.getCurToken()) {
            case LBRACKET:
            case CHAR:
                Tree or = Or();
                Tree concatPrime = ConcatPrime();
                return new Tree("Concat", or, concatPrime);
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree ConcatPrime() throws ParseException {
        switch (lex.getCurToken()) {
            case OR:
                lex.nextToken();
                Tree or = Or();
                Tree concatPrime = ConcatPrime();
                return new Tree("Concat'", new Tree("|"), or, concatPrime);
            case LBRACKET:
            case RBRACKET:
            case CHAR:
            case END:
                return new Tree("Concat'", new Tree("eps"));
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree Or() throws ParseException {
        switch (lex.getCurToken()) {
            case LBRACKET:
            case CHAR:
                Tree closure = Closure();
                Tree orPrime = OrPrime();
                return new Tree("Or", closure, orPrime);
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree OrPrime() throws ParseException {
        switch (lex.getCurToken()) {
            case CLOSURE:
//                lex.nextToken();
                Tree star = Star();
                Tree orPrime = OrPrime();
                return new Tree("Or'", star, orPrime); //, true), closurePrime);
            case OR:
            case LBRACKET:
            case RBRACKET:
            case CHAR:
            case END:
                return new Tree("Or'", new Tree("eps"));
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree Star() throws ParseException {
        switch (lex.getCurToken()) {
            case CLOSURE:
                lex.nextToken();
                return new Tree("Star", new Tree("*"));
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }

    private Tree Closure() throws ParseException {
        switch (lex.getCurToken()) {
            case LBRACKET:
                lex.nextToken();
                Tree R = R();
                if (lex.getCurToken() != Token.RBRACKET) {
                    throw new ParseException(") expected at position ", (lex.getCurPos() - 1));
                }
                lex.nextToken();
                return new Tree("Closure", new Tree("("), R, new Tree(")"));
            case CHAR:
                char c = (char) lex.getCurChar();
                lex.nextToken();
                return new Tree("Closure", new Tree(String.valueOf(c)));
            default:
                throw new ParseException("Unexpected character '" + ((char) lex.getCurChar()) + "'", (lex.getCurPos() - 1));
        }
    }
}
