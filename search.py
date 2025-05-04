import json
from collections import Counter
import re

def tokenize(text):
    return re.findall(r'\b\w+\b', text.lower())

def search(query, max_results=10):
    with open('index.json', 'r', encoding='utf-8') as f:
        data = json.load(f)

    index = data['index']
    documents = data['documents']
    query_terms = tokenize(query)

    doc_scores = Counter()

    for term in query_terms:
        if term in index:
            for doc_id, _ in index[term]:
                doc_scores[doc_id] += 1

    results = []
    for doc_id, score in doc_scores.most_common(max_results):
        content = documents[doc_id]
        snippet = content[:150].replace('\n', ' ') + '...'
        results.append({
            'title': doc_id,
            'url': f'https://stenoip.github.io/content/{doc_id}',
            'content': snippet
        })

    return results
