from fastapi import FastAPI, Query
from fastapi.middleware.cors import CORSMiddleware
from search import search

app = FastAPI()

# Allow frontend from GitHub Pages to access this backend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # You can narrow this to stenoip.github.io
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/search")
def search_endpoint(q: str = Query(..., min_length=1)):
    results = search(q)
    return {"hits": results}
