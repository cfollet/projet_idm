import com.pauware.pauware_engine._Core.*;
import com.pauware.pauware_engine._Exception.*;
import com.pauware.pauware_engine._Java_EE.*;

public class Voiture{

    private AbstractStatechart _Arret;
    private AbstractStatechart _Marche;
    private AbstractStatechart _Marche__MoinsVite;
    private AbstractStatechart _Marche__PlusVite;
    private AbstractStatechart _Marche__Maximum;
    private AbstractStatechart_monitor _Voiture;
    private int _vitesse;
    private boolean _maxAtteint;

    public  Voiture() throws Statechart_exception{
        _Arret = new Statechart("_Arret");
        _Arret.doActivity(this, "_Arret__Activity");
 
        _Marche__MoinsVite = new Statechart("_Marche__MoinsVite");
        _Marche__MoinsVite.doActivity(this, "_Marche__MoinsVite__Activity");
 
        _Marche__PlusVite = new Statechart("_Marche__PlusVite");
        _Marche__PlusVite.doActivity(this, "_Marche__PlusVite__Activity");
 
        _Marche__Maximum = new Statechart("_Marche__Maximum");
        _Marche__Maximum.doActivity(this, "_Marche__Maximum__Activity");
 
        _Marche__PlusVite.inputState();
        _Marche = (_Marche__MoinsVite.xor(_Marche__PlusVite.xor(_Marche__Maximum))).name("_Marche");
        _Arret.inputState();
        _Voiture = new Statechart_monitor(_Arret.xor(_Marche),"Voiture",AbstractStatechart_monitor.Show_on_system_out, null);
        this._maxAtteint = false;
    }
    public void start() throws Statechart_exception{
        _Voiture.fires("accelerer__Event", _Arret, _Marche, true);
        _Voiture.fires("ralentir__Event", _Marche, _Arret, this, "_Marche__Arret__Guard");
        _Voiture.fires("accelerer__Event", _Marche, _Marche__PlusVite, this, "_Marche__Marche__PlusVite__Guard");
        _Voiture.fires("accelerer__Event", _Marche, _Marche__Maximum, this, "_Marche__Marche__Maximum__Guard");
        _Voiture.fires("ralentir__Event", _Marche, _Marche__MoinsVite, this, "_Marche__Marche__MoinsVite__Guard");
        _Voiture.start();
    }
    public void stop() throws Statechart_exception{
        _Voiture.stop();
    }
    public void ralentir__Event() throws Statechart_exception{
        _Voiture.run_to_completion("ralentir__Event");
    }
    public void accelerer__Event() throws Statechart_exception{
        _Voiture.run_to_completion("accelerer__Event");
    }
    public void _Arret__Activity(){
        this._vitesse = 0;
 
    }
    public void _Marche__MoinsVite__Activity(){
        this._vitesse = (this._vitesse-10);
 
    }
    public void _Marche__PlusVite__Activity(){
        this._vitesse = (this._vitesse+10);
 
    }
    public void _Marche__Maximum__Activity(){
        this._vitesse = 100;
        this._maxAtteint = true;
 
    }
    public boolean _Marche__Arret__Guard(){
        return (this._vitesse==10);
    }
    public boolean _Marche__Marche__PlusVite__Guard(){
        return ((this._vitesse>=10)&&(this._vitesse<90));
    }
    public boolean _Marche__Marche__Maximum__Guard(){
        return (this._vitesse==90);
    }
    public boolean _Marche__Marche__MoinsVite__Guard(){
        return (this._vitesse!=10);
    }
}