/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export const CONVERSATIONS_LIST = [
  {
    "id": "12",
    "medico": {
      "nome": "Mario",
      "cognome": "Rossi",
      "codice_fiscale": "string"
    },
    "argomento": "Dolore al ginocchio",
    "n_messaggi_non_letti": 2,
    "sola_lettura": false,
    "ultimo_messaggio": {
      "id": "12",
      "data_creazione": "2020-02-07T14:14:39.420Z",
      "letto": true,
      "data_lettura": "2020-02-07T15:14:39.420Z",
      "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. ",
      "autore": {
        "nome": "string",
        "cognome": "string",
        "codice_fiscale": "string",
        "tipo": "assistito"
      }
    },
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "motivo_blocco": {
      "codice": "string",
      "descrizione": "string"
    },
    "motivazione_medico": "string",
    "autore": {
      "nome": "string",
      "cognome": "string",
      "codice_fiscale": "string",
      "tipo": "assistito"
    }
  },
  {
    "id": "13",
    "medico": {
      "nome": "Mario",
      "cognome": "Rossi",
      "codice_fiscale": "string"
    },
    "argomento": "Dolore al ginocchio",
    "n_messaggi_non_letti": 2,
    "sola_lettura": true,
    "ultimo_messaggio": {
      "id": "12",
      "data_creazione": "2020-02-07T14:14:39.420Z",
      "letto": true,
      "data_lettura": "2020-02-07T15:14:39.420Z",
      "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. ",
      "autore": {
        "nome": "string",
        "cognome": "string",
        "codice_fiscale": "string",
        "tipo": "medico"
      }
    },
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "motivo_blocco": {
      "codice": "string",
      "descrizione": "string"
    },
    "motivazione_medico": "string",
    "autore": {
      "nome": "string",
      "cognome": "string",
      "codice_fiscale": "string",
      "tipo": "medico"
    }
  }
]


export const MESSAGES_LIST = [
  {
    "id": "12",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string",
      "tipo": "assistito"
    },
    "modificato": false,
    "autore_modifica": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string"
    },
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
  {
    "id": "13",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Mario",
      "cognome": "Rossi",
      "codice_fiscale": "string",
      "tipo": "medico"
    },
    "modificato": true,
    "autore_modifica": "XXXXXXXX",
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
  {
    "id": "14",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string",
      "tipo": "assistito"
    },
    "modificato": true,
    "autore_modifica": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string"
    },
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
  {
    "id": "15",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Mario",
      "cognome": "Rossi",
      "codice_fiscale": "string",
      "tipo": "medico"
    },
    "modificato": true,
    "autore_modifica": {
      "nome": "string",
      "cognome": "string",
      "codice_fiscale": "string"
    },
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
  {
    "id": "16",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string",
      "tipo": "delegato"
    },
    "modificato": true,
    "autore_modifica": {
      "nome": "Nome",
      "cognome": "cognome",
      "codice_fiscale": "string"
    },
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
  {
    "id": "17",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
    "modificabile": true,
    "autore": {
      "nome": "Mario",
      "cognome": "Rossi",
      "codice_fiscale": "string",
      "tipo": "medico"
    },
    "modificato": false,
    "autore_modifica": {
      "nome": "string",
      "cognome": "string",
      "codice_fiscale": "string"
    },
    "data_modifica": "2020-02-07T14:14:39.420Z",
    "allegati": [
      {
        "id_documento_ilec": "string",
        "codice_cl": "string",
        "azienda": {
          "codice": "string",
          "descrizione": "string"
        },
        "descrizione_struttura": "string",
        "data_validazione": "2022-05-25",
        "tipo_documento": {
          "codice": "string",
          "descrizione": "string"
        },
        "tipo_contributo": "string",
        "categoria_tipologia": [
          {
            "categoria": "string",
            "tipologia": "string"
          }
        ],
        "id_episodio": 0,
        "codice_documento_dipartimentale": "string",
        "id_repository_cl": "string",
        "accession_numbers": [
          {
            "accession_number": "string",
            "data_notifica_pacs": "2022-05-25"
          }
        ]
      }
    ]
  },
]

export const COVERSATION_MESSAGES =[
  {id: '12', messages: MESSAGES_LIST},
  {id: '13', messages: MESSAGES_LIST}
]


export const CONVERSATION_NEW =   {
  "id": "14",
  "medico": {
    "nome": "Mario",
    "cognome": "Rossi",
    "codice_fiscale": "string"
  },
  "argomento": "",
  "n_messaggi_non_letti": 0,
  "sola_lettura": false,
  "ultimo_messaggio": {
    "id": "12",
    "data_creazione": "2020-02-07T14:14:39.420Z",
    "letto": true,
    "data_lettura": "2020-02-07T15:14:39.420Z",
    "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. ",
    "autore": {
      "nome": "string",
      "cognome": "string",
      "codice_fiscale": "string",
      "tipo": "assistito"
    }
  },
  "data_creazione": "2021-02-07T14:14:39.420Z",
  "motivo_blocco": {
    "codice": "string",
    "descrizione": "string"
  },
  "motivazione_medico": "string",
  "autore": {
    "nome": "string",
    "cognome": "string",
    "codice_fiscale": "string",
    "tipo": "assistito"
  }
}


export const NEW_MESSAGE = {
  "id": "12",
  "data_creazione": "2020-02-07T14:14:39.420Z",
  "letto": true,
  "data_lettura": "2020-02-07T15:14:39.420Z",
  "testo": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam et sagittis massa. Donec rutrum facilisis mi, in convallis metus pretium nec. Vestibulum ultricies tellus eget mi consequat, non pellentesque arcu pretium. Praesent ipsum nisl, porttitor eget erat a, suscipit cursus augue. Curabitur a finibus nisi, in tincidunt magna. Donec sed egestas odio. Mauris sem nisl, feugiat convallis ultricies quis, tempus a nunc",
  "modificabile": true,
  "autore": {
    "nome": "Nome",
    "cognome": "cognome",
    "codice_fiscale": "string",
    "tipo": "assistito"
  },
  "modificato": false,
  "autore_modifica": {
    "nome": "Nome",
    "cognome": "cognome",
    "codice_fiscale": "string"
  },
  "data_modifica": "2020-02-07T14:14:39.420Z",
  "allegati": [
    {
      "id_documento_ilec": "string",
      "codice_cl": "string",
      "azienda": {
        "codice": "string",
        "descrizione": "string"
      },
      "descrizione_struttura": "string",
      "data_validazione": "2022-05-25",
      "tipo_documento": {
        "codice": "string",
        "descrizione": "string"
      },
      "tipo_contributo": "string",
      "categoria_tipologia": [
        {
          "categoria": "string",
          "tipologia": "string"
        }
      ],
      "id_episodio": 0,
      "codice_documento_dipartimentale": "string",
      "id_repository_cl": "string",
      "accession_numbers": [
        {
          "accession_number": "string",
          "data_notifica_pacs": "2022-05-25"
        }
      ]
    }
  ]
}
