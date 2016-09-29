function (doc, meta) {
if(doc._class == "com.carnival.mm.domain.Medallion" && doc.status=="UNASSIGNED" && (doc.document_type=="current" || doc.document_type==null)){
emit(doc.status, doc);
}
}