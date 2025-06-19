export class BibliothequeTag {
  constructor({ tag, nombres }) {
    this.tag = tag
    this.nombres = nombres
  }
}

export class ElementDeScript {
  constructor({ id, nom, description, contenu, parametres, tags, date, historiques, categorie }) {
    this.id = id
    this.nom = nom
    this.description = description
    this.contenu = contenu
    this.parametres = parametres || []
    this.tags = tags
    this.date = new Date(date)
    this.historiques = historiques || []
    this.categorie = categorie
  }
}