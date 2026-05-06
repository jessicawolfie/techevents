from http.client import HTTPException
from typing import Optional
from fastapi import FastAPI
import json

# Inicializa o servidor FastAPI
app = FastAPI(title="Tech Events API")

# Lendo o arquivo JSON
with open("events_dataset.json", "r", encoding="utf-8") as f:
    dados = json.load(f)
    if isinstance(dados, list):
        lista_de_eventos = dados
    else:
        lista_de_eventos = dados.get("allEvents", [])

@app.get("/events")
def get_events(tipo: Optional[str] = None):    
    if tipo is None:
        return lista_de_eventos 
    
    eventos_filtrados = []
    
    for event in lista_de_eventos:
        # Converte para string minúscula para evitar erros de leitura ("True", "true", verdadeiro/falso)
        is_online_str = str(event.get("is_online", "")).lower()
        
        # Pega o tipo escrito também (caso exista)
        tipo_texto = str(event.get("type", "")).lower()
        
        # O evento é considerado online se is_online for "true" ou se a palavra "online" estiver no tipo
        eh_online = (is_online_str == "true") or ("online" in tipo_texto)
        
        # Lógica final de separação
        if tipo.strip().lower() == "online" and eh_online:
            eventos_filtrados.append(event)
        elif tipo.strip().lower() == "presencial" and not eh_online:
            eventos_filtrados.append(event)
            
    return eventos_filtrados

@app.get("/events/{event_id}")
def get_event_by_id(event_id: str):
    # Percorre a lista de eventos carregada do JSON
    for event in lista_de_eventos:
        # Se o ID do evento atual for igual ao ID que o Android pediu, devolve o evento
        if str(event.get("id")) == event_id:
            return event
            
    # Se o loop terminar e não encontrar nenhum evento com esse ID, 
    # devolve um erro 404 proativo e customizado.
    raise HTTPException(status_code=404, detail="Evento não encontrado no banco de dados.")