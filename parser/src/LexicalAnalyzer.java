import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

class LexicalAnalyzer {
    private InputStream input;
    private int curChar;
    private int curPos;
    private Token curToken;

    Token getCurToken() {
        return curToken;
    }

    int getCurPos() {
        return curPos;
    }

    int getCurChar() {
        return curChar;
    }

    LexicalAnalyzer(InputStream input) {
        this.input = input;
        curPos = 0;
    }

    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = input.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), (curPos - 1));
        }
    }

    void nextToken() throws ParseException {
        nextChar();

        while (isBlank(curChar)) {
            nextChar();
        }

        switch (curChar) {
            case '(':
                curToken = Token.LBRACKET;
                break;
            case ')':
                curToken = Token.RBRACKET;
                break;
            case '|':
                curToken = Token.OR;
                break;
            case '*':
                curToken = Token.CLOSURE;
                break;
            case -1:
                curToken = Token.END;
                return;
            default:
                if (Character.isLowerCase(curChar)) {
                    curToken = Token.CHAR;
                } else {
                    throw new ParseException("Illegal character '" + (char) curChar + "'", (curPos - 1));
                }
        }
    }
}
