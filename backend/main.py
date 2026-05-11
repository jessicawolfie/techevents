from http.client import HTTPException
from typing import Optional
from fastapi import FastAPI
import json
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

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


# MOTOR DE MACHINE LEARNING PARA RECOMENDAÇÃO DE EVENTOS
# Transformando os dados em um DataFrame para facilitar o processamento
df_eventos = pd.DataFrame(lista_de_eventos)

# Função para preparar o texto para a IA, combinando descrição e tags, e convertendo para minúsculas
def preparar_texto_para_ia(linha):
    descricao = str(linha.get("description", ""))
    tags = " ".join(linha.get("tags", [])) if isinstance(linha.get("tags"), list) else ""
    return (descricao + " " + tags).lower()

# Aplica a função de preparação de texto para cada linha do DataFrame
df_eventos['conteudo_ia'] = df_eventos.apply(preparar_texto_para_ia, axis=1)

# Cria a matriz TF-IDF e calcula a similaridade de cosseno entre os eventos
vectorizer = TfidfVectorizer()

# Treinamento: a IA aprende a partir do conteúdo preparado dos eventos, criando uma matriz de características (TF-IDF) e calculando a similaridade entre os eventos para futuras recomendações. 
tfidf_matrix = vectorizer.fit_transform(df_eventos['conteudo_ia'])
matriz_similaridade = cosine_similarity(tfidf_matrix, tfidf_matrix)


# ENDPOINT PARA RECOMENDAÇÃO DE EVENTOS
@app.get("/events/{event_id}/recommendations")
def get_recommendations(event_id: str):
    # Busca o índice do evento solicitado pelo ID
    indices = df_eventos.index[df_eventos['id'].astype(str) == event_id].tolist()

# Se o evento não for encontrado, retorna um erro 404
    if not indices:
        raise HTTPException(status_code=404, detail="Evento não encontrado para recomendações.")
    idx = indices[0]

    # Obtém as similaridades do evento solicitado com todos os outros eventos
    notas_similares = list(enumerate(matriz_similaridade[idx]))

    # Ordena os eventos por similaridade, do mais similar para o menos similar
    notas_ordenadas = sorted(notas_similares, key=lambda x: x[1], reverse=True)

    # Pega os índices dos 3 eventos mais similares (excluindo o próprio evento)
    top_3_indices = [tupla[0] for tupla in notas_ordenadas[1:4]]

    # Retorna os eventos recomendados com base nos índices encontrados
    recomendacoes = df_eventos.iloc[top_3_indices].drop(columns=['conteudo_ia']).to_dict(orient='records')
    return recomendacoes
