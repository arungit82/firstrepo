function (doc, meta) {
if(doc._class == "com.carnival.mm.domain.Medallion" && doc.reservationId && (doc.document_type=="current" || doc.document_type==null)){
emit(doc.reservationId.toLowerCase(), doc);
}
}