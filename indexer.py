import os
import json
import re
from collections import defaultdict
from pathlib import Path

def tokenize(text):
    text = text.lower()
    return re.findall(r'\b\w+\b', text)

def build_index(data_folder='data'):
    index = defaultdict(list)
    documents = {}

    for filename in os.listdir(data_folder):
        path = os.path.join(data_folder, filename)
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
            doc_id = filename
            documents[doc_id] = content
            words = tokenize(content)

            for pos, word in enumerate(words):
                index[word].append((doc_id, pos))

    with open('index.json', 'w', encoding='utf-8') as f:
        json.dump({'index': index, 'documents': documents}, f)

if __name__ == '__main__':
    build_index()
