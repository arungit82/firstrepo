function (doc, meta) {
if(doc._class == "com.carnival.mm.domain.Medallion" && (doc.document_type=="current" || doc.document_type==null)){
emit(doc.lastName.toLowerCase(), doc);
emit([doc.firstName.toLowerCase(), doc.lastName.toLowerCase()], doc);
}
}