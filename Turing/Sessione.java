package Turing;

class Sessione
{
  private Utente utente;
  private Stato stato;
  private String nomeDocumentoEdit;
  private int numSezioneEdit;

  enum Stato
  {
    Started,
    Logged,
    Editing
  }

  Sessione()
  {
    this.stato = Stato.Started;
    this.nomeDocumentoEdit = null;
    this.numSezioneEdit = -1;
  }

  Utente getUtente()
  {
    return utente;
  }

  Stato getStato()
  {
    return stato;
  }
  void setUtente(Utente utente)
  {
    this.utente = utente;
  }

  void setStato(Stato stato)
  {
    this.stato = stato;
  }

  public String getNomeDocumentoEdit()
  {
    return nomeDocumentoEdit;
  }

  public void setNomeDocumentoEdit(String nomeDocumentoEdit)
  {
    this.nomeDocumentoEdit = nomeDocumentoEdit;
  }

  public int getNumSezioneEdit()
  {
    return numSezioneEdit;
  }

  public void setNumSezioneEdit(int numSezioneEdit)
  {
    this.numSezioneEdit = numSezioneEdit;
  }
}
