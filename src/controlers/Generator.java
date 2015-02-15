package controlers;
import models.CrosswordDictionary;
import models.IEntry;
import models.Word;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tomek on 2014-12-25.
 */
public class Generator {
    private int X;
    private int Y;
    private char[][] matrix;
    public char[][] getMatrix(){
        return matrix;
    }
    CrosswordDictionary cd;
    public Generator(int _X, int _Y){
        cd = new CrosswordDictionary();
        generateBoard(_X,_Y);
        X = _X;
        Y = _Y;/*
        matrix = new char[X][Y];
        matrix[0][0]=' ';
        matrix[0][1]=' ';
        matrix[1][0]=' ';
        matrix[4][3]=' ';
        matrix[3][4]=' ';
        matrix[4][4]=' ';
        matrix[3][3]=' ';
        matrix[3][1]=' ';
        matrix[1][1]=' ';
        matrix[1][3]=' ';*/

        //generate();
    }

    private void generateBoard(int x, int y) {
        matrix = new char[x][y];
        for(int i=1;i<x;i+=2){
            for(int j=1;j<y;j+=2){
                matrix[i][j]=' ';
            }
        }
        final int BEST_LENGTH = 5;
        int division = (x+2)/(BEST_LENGTH+1);
        for(int i=1;i<division;i++){
            for(int j=0;j<y;j+=4) {
                matrix[-1+(BEST_LENGTH+1)*i][j]=' ';
            }
            for(int j=2;j<y;j+=4) {
                matrix[3][j]=' ';
                matrix[3+(BEST_LENGTH+1)*i][j]=' ';
            }
        }
        division = (y+2)/(BEST_LENGTH+1);
        for(int i=1;i<division;i++){
            for(int j=0;j<x;j+=4) {
                matrix[j][-1+(BEST_LENGTH+1)*i]=' ';
            }
            for(int j=2;j<x;j+=4) {
                matrix[j][3]=' ';
                matrix[j][3+(BEST_LENGTH+1)*i]=' ';
            }
        }
    }

    //check how much words match regex
    private void checkPossibilities(Word word){
        word.possibilities=0;
        for(String str: word.entries){
            char[] charArray = str.toCharArray();
            boolean fulfilled=true;
            for(int i=0;i<word.length;++i){
                if(word.word[i]!=' '&&word.word[i]!=0&&charArray[i]!=word.word[i]){
                    fulfilled = false;
                    break;
                }
            }
            if (fulfilled == true) {
                word.possibilities += 1;
            }
        }
    }

    private int calcPossibility(Word word){
        int poss=0;
        for(String str: word.entries){
            boolean fulfilled=true;
            char[] charArray = str.toCharArray();
            for(int i=0;i<word.length;++i){
                if(word.word[i]!=' '&&word.word[i]!=0&&charArray[i]!=word.word[i]){
                    fulfilled = false;
                    break;
                }
            }
            if (fulfilled == true) {
                poss += 1;
            }
        }
        return poss;
    }

    private int estimatePossibility(Word w, List<Word> list){
        int minPoss=Integer.MAX_VALUE;
        for(Word word : list){
            if((word.start.x<=w.start.x && word.end.x>=w.end.x) ||
                    (word.start.x>=w.start.x && word.end.x<=w.end.x)){
                if((word.start.y>=w.start.y && word.end.y<=w.end.y) ||
                        (word.start.y<=w.start.y && word.end.y>=w.end.y)){
                    //words cross
                    int numInWord=0;
                    int numInW=0;
                    if(word.start.x==word.end.x){
                        numInWord = w.start.y - word.start.y;
                        numInW = word.start.x - w.start.x;
                    } else {
                        numInWord = w.start.x - word.start.x;
                        numInW = word.start.y - w.start.y;
                    }
                    Word wordTmp= new Word();
                    wordTmp.start = word.start;
                    wordTmp.end = word.end;
                    wordTmp.length = word.length;
                    wordTmp.entries = word.entries;
                    wordTmp.isFilled = false;
                    wordTmp.word = new char[word.length];
                    for(int ii=0;ii<word.length;++ii){
                        wordTmp.word[ii] = word.word[ii];
                    }
                    wordTmp.word[numInWord] = w.word[numInW];
                    int tmpPoss = calcPossibility(wordTmp);
                    //System.out.print(String.valueOf(wordTmp.word)+tmpPoss+" ");
                    if(tmpPoss<minPoss){
                        minPoss=tmpPoss;
                    }
                }
            }
        }

        //System.out.println("("+String.valueOf(w.word)+")");
        return minPoss;
    }

    private void findPossibilities(Word w, List<Word> list){
        for(Word word : list){
            if((word.start.x<=w.start.x && word.end.x>=w.end.x) ||
                    (word.start.x>=w.start.x && word.end.x<=w.end.x)){
                if((word.start.y>=w.start.y && word.end.y<=w.end.y) ||
                        (word.start.y<=w.start.y && word.end.y>=w.end.y)){
                    //words cross
                    int numInWord=0;
                    int numInW=0;
                    if(word.start.x==word.end.x){
                        numInWord = w.start.y - word.start.y;
                        numInW = word.start.x - w.start.x;
                    } else {
                        numInWord = w.start.x - word.start.x;
                        numInW = word.start.y - w.start.y;
                    }
                    word.word[numInWord] = w.word[numInW];
                    checkPossibilities(word);
                }
            }

        }

    }

    private List<Word> findAllWordsInGrid(){
        List<Word> list = new ArrayList<Word>();
        for(int i=0;i<Y;++i){
            char prev=' ';
            char current=matrix[X-1][i];
            int j=X-2;
            boolean wordFlag=false;
            Word tmpWord=null;
            while(j>=0){
                if(matrix[j][i]!=' '&&current!=' '&&prev==' '){
                    tmpWord = new Word();
                    tmpWord.isFilled = false;
                    wordFlag = true;
                    tmpWord.end = new Point(j+1,i);
                    list.add(tmpWord);
                }
                if(matrix[j][i]==' '){
                    if(wordFlag==true&&tmpWord!=null){
                        tmpWord.start = new Point(j+1,i);
                        tmpWord.length = tmpWord.end.x - tmpWord.start.x + 1;
                        tmpWord.word = new char[tmpWord.length];
                    }
                    wordFlag=false;
                }
                prev=current;
                current=matrix[j][i];
                --j;
            }
            if(wordFlag==true){
                tmpWord.start = new Point(j+1,i);
                tmpWord.length = tmpWord.end.x - tmpWord.start.x + 1;
                tmpWord.word = new char[tmpWord.length];
            }
        }
        for(int i=0;i<X;++i){
            char prev=' ';
            char current=matrix[i][Y-1];
            int j=Y-2;
            boolean wordFlag=false;
            Word tmpWord=null;
            while(j>=0){
                if(matrix[i][j]!=' '&&current!=' '&&prev==' '){
                    tmpWord = new Word();
                    tmpWord.isFilled = false;
                    wordFlag = true;
                    tmpWord.end = new Point(i,j+1);
                    list.add(tmpWord);
                }
                if(matrix[i][j]==' '){
                    if(wordFlag==true&&tmpWord!=null){
                        tmpWord.start = new Point(i,j+1);
                        tmpWord.length = tmpWord.end.y - tmpWord.start.y + 1;
                        tmpWord.word = new char[tmpWord.length];
                    }
                    wordFlag=false;
                }
                prev=current;
                current=matrix[i][j];
                --j;
            }
            if(wordFlag==true){
                tmpWord.start = new Point(i,j+1);
                tmpWord.length = tmpWord.end.y - tmpWord.start.y + 1;
                tmpWord.word = new char[tmpWord.length];
            }
        }
        return list;
    }

    public void generate(){
        List<Word> list = findAllWordsInGrid();
        for (Word w : list){
            //System.out.println(w.start.x+" "+w.start.y+" "+w.end.x+" "+w.end.y+" "+w.length);
        }
        Random r = new Random();
        int random = r.nextInt(list.size());
        for(Word w: list){
            getWords(w);
        }

        Word word = findFirstWord(random, list);
        for(Word w: list){
            checkPossibilities(w);
        }
        //findPossibilities(word, list);
        while(isEverythingFilled(list)){
            findPossibilities(word, list);
            word = findWordWithSmallestPossibility(list);
            if(word == null){
                return;
            }
        }

    }
    private void endProgram(List<Word> list){
        for(Word ww: list){
            int i=0;
            for(char c:ww.word) {
                if(c==' '||c==0){
                    //System.out.print('X');
                    c=' ';
                } else {
                    //System.out.print(c);
                }
                if(ww.start.x==ww.end.x){
                    matrix[ww.start.x][ww.start.y+i]=c;
                } else {
                    matrix[ww.start.x+i][ww.start.y] = c;
                }
                ++i;
            }
            //System.out.println();
        }/*
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (matrix[x][y] == '\u0000') System.out.print(' ');
                else
                    System.out.print(matrix[x][y]);
            }

            System.out.println();
        }*/
        //System.exit(0);
    }

    private void getWords(Word w) {
        w.entries = new ArrayList<String>();
        for(IEntry entry: cd.getEntries()) {
            String str = entry.getWord();
            boolean fulfilled = true;
            if (str.length() != w.length) {
                continue;
            }
            w.entries.add( str);
        }
    }
    private void divideWord(Word w){
        switch(w.length) {
            case 4:
            case 6:
            case 8:
            case 10:
                w.length-=1;
                if(w.start.x==w.end.x){
                    if(w.start.y%2==0){
                        w.end.y-=1;
                    } else {
                        w.start.y+=1;
                        for(int i=0;i<w.length;++i) {
                            w.word[i]=w.word[i+1];
                        }
                    }
                } else {
                    if(w.start.x%2==0){
                        w.end.x-=1;
                    } else {
                        w.start.x+=1;
                        for(int i=0;i<w.length;++i) {
                            w.word[i]=w.word[i+1];
                        }
                    }
                }
                break;
            case 5:
            case 7:
            case 9:
                w.length-=2;
                if(w.start.x==w.end.x){
                    w.end.y-=2;
                } else {
                    w.end.x-=2;
                }
                break;
            default:
                w.isFilled = true;
                break;
        }
        checkPossibilities(w);
    }
    private Word findWordWithSmallestPossibility(List<Word> list) {
        double possibility = Double.MAX_VALUE;
        int num = -1;
        for(Word w: list){
            //System.out.println( String.valueOf(w.word)+": "+w.possibilities);
            if(w.possibilities < possibility && w.isFilled==false){
                possibility = w.possibilities;
                num = list.indexOf(w);
            }
        }
        //System.out.println("------");
        if(num==-1){

            endProgram(list);

            return null;
        }
        Word w = list.get(num);
        possibility= Double.MIN_VALUE;
        Word tmp = new Word();
        if ( w.possibilities > 0){
            String s="";
            for(String str: w.entries){
                boolean fulfilled=true;
                char[] charArray = str.toCharArray();
                for(int i=0;i<w.length;++i){
                    if(w.word[i]!=' '&&w.word[i]!=0&&charArray[i]!=w.word[i]){
                        fulfilled = false;
                        break;
                    }
                }
                if (fulfilled == true){
                    tmp.start = w.start;
                    tmp.end = w.end;
                    tmp.length = w.length;
                    tmp.entries = w.entries;
                    tmp.isFilled = false;
                    tmp.word = new char[w.length];
                    for(int i=0;i<charArray.length;++i){
                        tmp.word[i]=charArray[i];

                    }
                    if(possibility<estimatePossibility(tmp,list)) {
                        s = str;
                    }
                }
            }
            if(!s.equals("")) {
                char[] charArray = s.toCharArray();
                for (int i = 0; i < w.length; ++i) {
                    w.word[i] = charArray[i];
                }
                w.isFilled = true;
            } else {
                w.isFilled = true;
                //endProgram(list);
            }
        } else {
            //w.isFilled = true;
            divideWord(w);
            //endProgram(list);
        }
        return w;
    }

    private boolean isEverythingFilled(List<Word> list) {
        //boolean retValue = true;
        for(Word w: list){
            for(int i=0;i<w.length;++i){
                if(w.word[i]==' '){
                    return false;
                }
            }
        }
        return true;
    }

    private Word findFirstWord(int random, List<Word> list) {
        double possibility= Double.MIN_VALUE;
        Word tmp = new Word();
        random = random % list.size();
        Word w = list.get(random);
        String s="";
        int ii=0;
        for(String str: w.entries){
            ++ii;
            boolean fulfilled=true;
            if(str.length()!=w.length){
                continue;
            }
            char[] charArray = str.toCharArray();
            if (fulfilled == true){
                tmp.start = w.start;
                tmp.end = w.end;
                tmp.length = w.length;
                tmp.entries = w.entries;
                tmp.isFilled = false;
                tmp.word = new char[w.length];
                for(int i=0;i<charArray.length;++i){
                    tmp.word[i]=charArray[i];

                }
                if(possibility<estimatePossibility(tmp,list)) {
                    s = str;
                }
            }
        }
        if(!s.equals("")) {
            char[] charArray = s.toCharArray();
            for (int i = 0; i < w.length; ++i) {
                w.word[i] = charArray[i];
            }
            w.isFilled = true;
        }
        return w;
    }

}
