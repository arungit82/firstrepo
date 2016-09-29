function (doc, meta) {
if(doc._class == "com.carnival.mm.domain.Medallion" && doc.hardwareId && (doc.document_type=="current" || doc.document_type==null)){
emit(doc.hardwareId, doc);
}
}