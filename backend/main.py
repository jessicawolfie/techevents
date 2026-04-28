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
    print(f"\n--- NOVA REQUISIÇÃO ---")
    print(f"O Android pediu o tipo: '{tipo}'")
    
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
            
    print(f"Filtro aplicado! Devolvendo {len(eventos_filtrados)} eventos.")
    return eventos_filtrados