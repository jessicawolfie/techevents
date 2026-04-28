from typing import Optional
from fastapi import FastAPI
import json

# Inicializa o servidor FastAPI
app = FastAPI(title="Tech Events API")

# Lendo o arquivo JSON
with open("events_dataset.json", "r", encoding="utf-8") as f:
    dados = json.load(f)
    lista_de_eventos = dados.get("allEvents", [])

@app.get("/events")
def get_events(tipo: Optional[str] = None):
    # Se não passar tipo (tipo=None), retorna a lista completa
    if tipo is None:
        return {"allEvents": lista_de_eventos}
    
    # Prepara a lista filtrada
    eventos_filtrados = []
    
    for event in lista_de_eventos:
        is_online = event.get("is_online")
        
        # Se pedirem "online" e o evento for online (True)
        if tipo.lower() == "online" and is_online == True:
            eventos_filtrados.append(event)
            
        # Se pedirem "presencial" e o evento não for online (False)
        elif tipo.lower() == "presencial" and is_online == False:
            eventos_filtrados.append(event)
            
    return {"allEvents": eventos_filtrados}
