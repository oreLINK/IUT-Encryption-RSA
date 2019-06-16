import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    //Pour RSA
    public static BigInteger p;
    public static BigInteger q;
    public static BigInteger n;
    public static BigInteger phiN;
    public static BigInteger e;
    public static BigInteger d;

    //Pour Diffie Hellman
    public static BigInteger p2;
    public static BigInteger g;
    public static BigInteger a;
    public static BigInteger b;

    //Pour Empreinte
    private static BigInteger pAlice;
    private static BigInteger qAlice;
    private static BigInteger nAlice;
    private static BigInteger phiNAlice;
    private static BigInteger eAlice;
    private static BigInteger dAlice;
    private static BigInteger pBob;
    private static BigInteger qBob;
    private static BigInteger nBob;
    private static BigInteger phiNBob;
    private static BigInteger eBob;
    private static BigInteger dBob;


    public static void main(String [] args){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!RSA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
        p= trouverNombrePremier();
        //p=1499;
        System.out.println("p : "+p);
        //q=3167;
        q= trouverNombrePremier();
        System.out.println("q : "+q);
        n=p.multiply(q);
        System.out.println("n : "+n);
        phiN=(p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        System.out.println("phiN : "+phiN);
        ArrayList<BigInteger> l = checkPremier(phiN);
        e=l.get(0);
        d=l.get(1);
        System.out.println("Clé publique : ("+ e +","+n+")");
        System.out.println("Clé privé : "+d);
        BigInteger test = getRandom(16);
        System.out.println("message : "+test);
        BigInteger testCrypte = puissanceModulaire(test,e,n);
        System.out.println("message chiffré : "+testCrypte);
        BigInteger testDecrypte = puissanceModulaire(testCrypte,d,n);
        System.out.println("message déchiffré :"+testDecrypte);
        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!FINRSA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!Diffie Hellman!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        p2 = trouverNombrePremier();
        System.out.println("p : "+ p2);
        g = trouverPremierAvec(p2);
        System.out.println("g : "+g);
        a=getRandom(32);
        b=getRandom(32);
        BigInteger gPuissA = puissanceModulaire(g,a,p2);
        System.out.println("g puissance a :"+ gPuissA);
        BigInteger gPuissB = puissanceModulaire(g,b,p2);
        BigInteger gPuissBA = puissanceModulaire(gPuissB,a,p2);
        System.out.println("g puissance b puissance a :"+ gPuissBA);
        BigInteger gPuissAB = puissanceModulaire(gPuissA,b,p2);
        System.out.println("g puissance a puissance b :"+ gPuissAB);

        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!Fin Diffie Hellman!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!Emprunte!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        pAlice= trouverNombrePremier();
        qAlice= trouverNombrePremier();
        nAlice=pAlice.multiply(qAlice);
        phiNAlice=(pAlice.subtract(BigInteger.ONE)).multiply(qAlice.subtract(BigInteger.ONE));
        ArrayList<BigInteger> l2 = checkPremier(phiNAlice);
        eAlice = l2.get(0);
        dAlice = l2.get(1);
        System.out.println("Clé publique Alice: ("+ eAlice +","+nAlice+")");
        System.out.println("Clé privé Alice: "+dAlice);

        pBob= trouverNombrePremier();
        qBob= trouverNombrePremier();
        nBob=pBob.multiply(qBob);
        phiNBob=(pBob.subtract(BigInteger.ONE)).multiply(qBob.subtract(BigInteger.ONE));
        ArrayList<BigInteger> l3 = checkPremier(phiNBob);
        eBob= l3.get(0);
        dBob= l3.get(1);
        System.out.println("Clé publique Bob: ("+ eBob +","+nBob+")");
        System.out.println("Clé privé Bob: "+dBob);

        BigInteger messageAlice = getRandom(16);
        System.out.println("message : "+messageAlice);
        BigInteger empreinte= messageAlice.mod(BigInteger.valueOf(13));
        System.out.println("empreinte : "+empreinte);


        BigInteger empreinteChiffreMoitie = puissanceModulaire(empreinte,dAlice,nAlice);
        System.out.println("empreinte chiffré a moitié : "+ empreinteChiffreMoitie);

        BigInteger empreinteChiffre = puissanceModulaire(empreinteChiffreMoitie,eBob,nBob);
        System.out.println("empreinte chiffré : "+ empreinteChiffre);

        BigInteger messageChiffre = puissanceModulaire(messageAlice,eBob,nBob);
        System.out.println("message chiffré : "+ messageChiffre);

        BigInteger empreinteDechiffreMoitie = puissanceModulaire(empreinteChiffre,dBob,nBob);
        System.out.println("empreinte déchiffré à moitié :" + empreinteDechiffreMoitie);
        BigInteger empreinteDechiffre = puissanceModulaire(empreinteDechiffreMoitie,eAlice,nAlice);
        System.out.println("message déchiffré : "+puissanceModulaire(messageChiffre,dBob,nBob));
        System.out.println("empreinte déchiffré: "+ empreinteDechiffre);
        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!Fin Emprunte!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

    private static BigInteger trouverPremierAvec(BigInteger p) {
        boolean trouve = false;
        BigInteger gTemp= null;
        while (!trouve){
            gTemp = getRandom(32);
            if (pgcd(p, gTemp).equals(BigInteger.ONE)){
                trouve=true;
            }
        }
        return gTemp;
    }

    private static BigInteger pgcd(BigInteger m, BigInteger n) {
        BigInteger r = null;
        while (!n.equals(BigInteger.ZERO)){
            r = m.mod(n);
            m=n;
            n=r;
        }
        return m;
    }

    private static BigInteger getRandom(int nbrBit){
        Random r = new Random();
        BigInteger b = new BigInteger(32,r);
        return b;
    }

    private static ArrayList<BigInteger> checkPremier(BigInteger phiN){
        boolean found = false;
        ArrayList<BigInteger> list = new ArrayList<>();
        BigInteger generatedLong;
        while (!found){
            generatedLong = getRandom(32);
            BigInteger [] bezout = bezout(generatedLong,phiN);
            if (bezout[2].compareTo(BigInteger.ONE)==0){
                found=true;
                list.add(generatedLong);
                list.add(bezout[0]);
            }
        }
        return list;
    }
    private static BigInteger trouverNombrePremier(){
        boolean found = false;
        BigInteger valRetour = BigInteger.ZERO;
        List<BigInteger> listNbPremiers = new ArrayList<BigInteger>(){{
            add(BigInteger.valueOf(2));
            add(BigInteger.valueOf(3));
            add(BigInteger.valueOf(5));
            add(BigInteger.valueOf(7));
            add(BigInteger.valueOf(11));
        }};
        while (!found){
            found = true;
            BigInteger generatedLong = getRandom(32);
            for (BigInteger i : listNbPremiers) {
                if(!puissanceModulaire(i, generatedLong.subtract(BigInteger.ONE), generatedLong).equals(BigInteger.ONE)){
                    found = false;
                }

            }
            valRetour = generatedLong;
        }
        return valRetour;
    }

    static BigInteger puissanceModulaire(BigInteger a, BigInteger e, BigInteger n)
    {
        BigInteger p;
        for(p=BigInteger.ONE; e.compareTo(BigInteger.ZERO) > 0; e=e.divide(BigInteger.valueOf(2))) {
            if (!e.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
                p=(a.multiply(p)).mod(n);
            a=(a.multiply(a)).mod(n);
        }
        return p;
    }

    public static BigInteger[] bezout(BigInteger a, BigInteger b){
        // La boucle principale:
        BigInteger phiN = b;
        BigInteger p = BigInteger.ONE;
        BigInteger q = BigInteger.valueOf(0);
        BigInteger r = BigInteger.valueOf(0);
        BigInteger s = BigInteger.valueOf(1);


        while (b != BigInteger.ZERO) {
            BigInteger c = a.mod(b);
            BigInteger quotient = a.divide(b);
            a = b;
            b = c;
            BigInteger nouveau_r = p.subtract(quotient.multiply(r));
            BigInteger nouveau_s = q.subtract(quotient.multiply(s));
            p = r; q = s;
            r = nouveau_r;
            s = nouveau_s;
        }
        if (p.compareTo(BigInteger.ZERO) <0 ){
            p=p.add(phiN);
        }
        BigInteger []result= {p,q,a};

        return result;
    }
}
