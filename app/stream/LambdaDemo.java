package stream;

import akka.stream.TLSClientAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by dirceu on 09/11/16.
 */
public class LambdaDemo {
    private static LambdaDemo ourInstance = new LambdaDemo();

    public static LambdaDemo getInstance() {
        return ourInstance;
    }

    private LambdaDemo() {
        List<String> strings = new ArrayList<String>();
        strings.add("rafa");

        strings.add("Rafael Claus");
        strings.add("Henrique");
        strings.add("Lucas");


        for(String valor:strings) {
            System.out.println(valor);
        }

        strings.forEach(valor -> System.out.println(valor));

        List<Integer> tamanhos = new ArrayList<Integer>();
        for(int index = 0 ; index<strings.size(); index++) {
            String valor = strings.get(index);
            tamanhos.add(valor.length());
        }

        Stream<Integer> tamanhosLambda = strings
                .stream().map(valor -> {
            return valor.length();
        });

        Optional<String> temString = Optional.of("Valor");
//        String outracoisa = temString.orElseGet("outracoisa");
        temString.map(valor ->
        {
            return valor.length();
        });
    }

    public static void main(String[] args){
        new LambdaDemo();
    }
}
