from fastapi import FastAPI
import json

# Inicializa o servidor FastAPI
app = FastAPI(title = "Tech Events API")

# Cria a rota que o app irá acessar para obter os dados dos eventos
@app.get("/events")
def get_events():
    # Lê o arquivo JSON contendo os dados dos eventos
    with open("events_dataset.json", "r", encoding="utf-8") as file:
        eventos = json.load(file)
    
    # Retorna os dados dos eventos como resposta da API
    return eventos
