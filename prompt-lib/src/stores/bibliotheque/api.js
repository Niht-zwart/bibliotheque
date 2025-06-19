import http from '@/http'

import { ElementDeScript } from './model'

const API_BASE = '/bibliotheques'
const DOCUMENTATION_BASE = '/documentation'

export const fetchBibliotheque = async () => {
  const res = await http.get(API_BASE)
  return res.data
}

export const fetchElementsByTag = async (categorie, tag) => {
  const res = await http.get(`${API_BASE}/${categorie}/tag/${tag}`)
  return res.data.map(e => new ElementDeScript({ ...e, categorie }))
}

export const fetchElementById = async (id) => {
  const res = await http.get(`${API_BASE}/${id}`)
  return new ElementDeScript(res.data)
}

export async function createPrompt(promptData) {
  try {
    const response = await http.post(`${API_BASE}`, promptData)
    return response.data
  } catch (error) {
    console.error('Erreur lors de la création du prompt :', error)
    throw error
  }
}

export async function fetchTagsByNomAndCategorie(nom, categorie) {
  try {
    const response = await http.get(`${API_BASE}/${categorie}/tags?nom=${nom}`)
    return response.data
  } catch (error) {
    console.error('Erreur lors de la création du prompt :', error)
    throw error
  }
}

export const fetchCategorie = async (categorie) => {
  const res = await http.get(`${API_BASE}/categories?nom=${categorie}`)
  return res.data
}

export const fetchDocumentCategories = async () => {
  const res = await http.get(`${DOCUMENTATION_BASE}/categories`)
  return res.data
}

export const fetchDocumentsByCategorie = async (categorie) => {
  const res = await http.get(`${DOCUMENTATION_BASE}/documents?categorie=${categorie}`)
  return res.data
}

export const saveCategorieDocuments = async (categorie) => {
  await http.post(`${DOCUMENTATION_BASE}/categories?categorie=${categorie}`, {})
}

export const saveDocument = async (document) => {
  await http.post(`${DOCUMENTATION_BASE}/documents`, document)
}