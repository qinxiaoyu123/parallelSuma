package com.tju.suma.test;

import com.tju.suma.axiomProcessor.Processor;
import com.tju.suma.dictionary.Dictionary;
import com.tju.suma.io.DictionaryInput;
import com.tju.suma.io.DictionaryOutput;
import com.tju.suma.jenaQuery.RewriteThing;
import com.tju.suma.reason.DicSerialReason;
import com.tju.suma.reason.SameAsReason;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.tju.suma.jenaQuery.JenaTest.jenaQuerySimple;

public class ParallelJar {
    private static Logger log = Logger.getLogger(SUMARunTest.class);
    public static void main(String[] args) throws Exception {
        if(args.length<6){
            System.out.println("Please enter args as follows: pathTBox pathABox pathExtendedABox eachThreadDataSize threadSize isQueryByJena queryPath");
            return;
        }
        String pathTBox = args[0];
        String pathABox = args[1];
        String pathExtendedABox = args[2];
        int eachThreadDataSize = Integer.parseInt(args[3]);
        int threadSize = Integer.parseInt(args[4]);
        String pathDataThing = "newThing.nt";
        boolean isQueryByJena = Boolean.parseBoolean(args[5]);
        initIsRoleWriting(true);
        String queryPath = args[6];
        String answerPath = "result_new.nt";

        int n_step = 7;

        DictionaryInit();

        preProcessRules(pathTBox);
        preProcessData(pathABox);

        outPutDictionaryToFile();

        materialization(n_step, eachThreadDataSize, threadSize);
        readDictionaryInMemory();



//        DictionaryOutput.sameAsMap("data/sameas.txt");
        writeFile(pathExtendedABox);

//        System.out.println("ObjectSomeValuesFromReason.objectSomeVaule "+ObjectSomeValuesFromReason.objectSomeVaule);
//        System.out.println("ObjectSomeValuesFromReason.objectSomeretun "+ObjectSomeValuesFromReason.objectSomeretun);
        if (isQueryByJena) {
            log.info("----------------------Start Query--------------------------");
            queryByJena(pathExtendedABox, pathDataThing, queryPath, answerPath);
        }
    }

    private static void DictionaryInit() {
        Dictionary.init();
    }

    private static void preProcessRules(String pathTBox) throws OWLOntologyCreationException {
        DictionaryInput.readTBox(pathTBox);
    }

    private static void preProcessData(String pathABox) throws IOException {
        long startTime1 = System.currentTimeMillis();
        DictionaryInput.readABox(pathABox);
        long startTime2 = System.currentTimeMillis();
        log.info("reading data time: " + (startTime2 - startTime1) + " ms");
    }

    private static void outPutDictionaryToFile() throws IOException {
        DictionaryOutput.encodeMap("encode.txt");
    }

    public static void materialization(int n_step, int eachThreadDataSize, int threadSize) throws ExecutionException, InterruptedException {
        long startTime3 = System.currentTimeMillis();
        Parallel.reason(n_step, eachThreadDataSize, threadSize);
        long startTime4 = System.currentTimeMillis();
        log.info("reason time: " + (startTime4 - startTime3) + " ms");
        SameAsReason.addEquivIndividual();
    }

    private static void readDictionaryInMemory() throws IOException {
        DictionaryInput.readDictionary("encode.txt");
    }

    public static void initIsRoleWriting(boolean isRoleWriting) {
        Processor.isRoleWriting = isRoleWriting;
    }

    private static void writeFile(String pathData) throws IOException {
        DictionaryOutput.outWriteDicDataMap(pathData);
    }

    private static void queryByJena(String pathABox, String pathABoxThing, String queryPath, String answerPath) throws IOException {
        RewriteThing.rewriteThing(pathABox, pathABoxThing);
        jenaQuerySimple(pathABoxThing, queryPath, answerPath);
    }
}
