package net.bruncle.wordinary;
import com.cloudgarden.speech.userinterface.*;

import javax.speech.*;
import javax.speech.synthesis.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Demonstrates simple speech synthesis
 */
public class SynthEngine {
    
    public SynthEngine(String descName){
        
	synth = null;
	try {
	    EngineList list = Central.availableSynthesizers(null);
	    SynthesizerModeDesc desc = null;	 
            
            descName = descName.replaceAll("[\\\\]","");
            //System.out.println("Searching for " + descName);
	    for(int i=0;i<list.size();i++) {
                //System.out.println("Number " + i + ": " + WordinaryFrame.descToString((SynthesizerModeDesc)list.elementAt(i)));
		if (WordinaryFrame.descToString((SynthesizerModeDesc)list.elementAt(i)).indexOf(descName) > -1
                        && !descName.equals(""))
			desc = ((SynthesizerModeDesc)list.elementAt(i));
	    }
	    if (desc == null){
	    	System.out.println("failed to load successfully: " + descName);
	    	loadedSuccessfully = false;
	    	return;
	    }
	    	
	    Voice[] voices = desc.getVoices();
		for(int j=0;j<voices.length;j++) {
		    if(voices[j].getName().indexOf("SAPI4") > 0 || (voices[j].getName().indexOf("SAPI5") > 0)) {
			voice = voices[j];
			break;
		    }
		}
	    synth = Central.createSynthesizer(desc);
	    synth.allocate();
	    synth.resume();
	    synth.waitEngineState(Synthesizer.ALLOCATED);
	    SynthesizerProperties props = synth.getSynthesizerProperties();
	    props.setVoice(voice);
	    props.setVolume(40.0f);
	    props.setSpeakingRate(100.0f);
	} catch(Exception e) {
	    e.printStackTrace(System.out);
	} catch(Error e1) {
	    e1.printStackTrace(System.out);
	} 
    }
    private boolean loadedSuccessfully;
    public boolean loadedSuccessfully(){
    	return loadedSuccessfully;
    }
    
    public void finalise(){
	    try {
	    	    System.out.println("Finished");
		    synth.waitEngineState(synth.QUEUE_EMPTY);
		    synth.deallocate();
		    synth.waitEngineState(synth.DEALLOCATED);
	    } catch(Exception e2) {}
     }
    
    public void saySomething(String toSay){
    	try{
    	    if (toSay == null || (toSay.equals(lastSaid) && !toSay.startsWith("The word is")) || synth == null){
    	    	return;
    	    }
    	    lastSaid = toSay;
    	    synth.speakPlainText(toSay,null);
	    synth.waitEngineState(synth.QUEUE_EMPTY);
	}
	catch (Exception e){
		e.printStackTrace();
	}
    }
    
    public void waitUntilSafe(){
    	if (synth != null)
            synth.cancelAll();
    }
    
    public void setEngine(String descName){
    	synth = null;
	try {
	    EngineList list = Central.availableSynthesizers(null);
	    SynthesizerModeDesc desc = null;	 
	    for(int i=0;i<list.size();i++) {
		if (WordinaryFrame.descToString((SynthesizerModeDesc)list.elementAt(i)).equals(descName))
			desc = ((SynthesizerModeDesc)list.elementAt(i));
	    }
	    if (desc == null){
	    	System.out.println("failed to load successfully: " + descName);
	    	loadedSuccessfully = false;
	    	return;
	    }
	    	
	    Voice[] voices = desc.getVoices();
		for(int j=0;j<voices.length;j++) {
		    if(voices[j].getName().indexOf("SAPI4") > 0 || (voices[j].getName().indexOf("SAPI5") > 0)) {
			voice = voices[j];
			break;
		    }
		}
	    synth = Central.createSynthesizer(desc);
	    synth.allocate();
	    synth.resume();
	    synth.waitEngineState(Synthesizer.ALLOCATED);
	    SynthesizerProperties props = synth.getSynthesizerProperties();
	    props.setVoice(voice);
	    props.setVolume(40.0f);
	    props.setSpeakingRate(100.0f);
	} catch(Exception e) {
	    e.printStackTrace(System.out);
	} catch(Error e1) {
	    e1.printStackTrace(System.out);
	} 
    }
    
    Synthesizer synth;
    Voice voice;
    String lastSaid;
}
