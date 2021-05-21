var app = new Vue({
    el: '#app',
    data:
        {
            error: "",
            info: "",
            contactInput: "", // Hält die Eingabe
            contact: {
                gender: "",
                salutation: "",
                letterSalutation: "",
                title: "",
                firstName: "",
                lastName: ""
            },
            emptyContact: {
                gender: "",
                salutation: "",
                letterSalutation: "",
                title: "",
                gender: "",
                firstName: "",
                lastName: ""
            },
            contactSave: {},
            allContacts: [],
            titleInput: "",
        },
	computed: {
        // Bestimmt welche Texte auf den Knöpfen angezeigt werden
        savePhrase: function() {
            // Verweise müssen über JSON ausgetauscht werden, da ansonsten nur der Verweis übergeben wird
            if (JSON.stringify(this.contact) === JSON.stringify(this.emptyContact)) {
                if(JSON.stringify(this.contactSave) === JSON.stringify(this.emptyContact))
                {
                    return ["Speichern nicht möglich", "Leere Eingabe"]
                }
                return ["Leere Eingabe", "Wiederherstellen"]
            } else if (JSON.stringify(this.contact) === JSON.stringify(this.contactSave))
            {
                return ["Speichern","Eingabe leeren"]
            } else if(JSON.stringify(this.contactSave) === JSON.stringify(this.emptyContact))
            {
                return ["Manuellen Eintrag speichern", "Eingabe verwerfen"]
            } else {
                return ["Änderungen speichern", "Änderungen verwerfen"]
            }
        },
	},
    mounted() {
        this.contactSave = JSON.parse(JSON.stringify(this.contact)) 
    },
    methods:
        {
            // Zeigt den Hinweistext an (info: Hinweis | error: Fehlermeldung)
            log: function (log, logType){
                this.info = ""
                this.error = ""
                if (logType === "error" || logType === "Error"){
                    this.error = log
      
                } else if (logType === "info" || logType === "Info") {
                    this.info = log
                }
            },
            // Um den Hinweistext (sowohl Hinweis als auch Fehler) auszublenden
            deletelog: function () {
                this.info = ""
                this.error = ""
            },
            // Sendet den an "contactInput" gebundenen Kontakt an das Backend und schiebt diese in "phoneNumberOutput", welches diese anzeigt
            checkContact: async function (){
                this.deletelog();
                if(this.contactInput.length < 2)
                {
                    this.log("Die Eingabe ist zu kurz.", "error")
                    return;
                }
                try {
                    let response = await fetch("http://localhost:5000/contact", {
                        body: JSON.stringify({contactString: this.contactInput}),
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        }
                    });
                
                    if (response.ok) { // wenn der HTTP-Status 200-299 ist (aktuell kommt nur 200)
                        let json = await response.json();
                        this.contact = json;
                        this.contactSave;
                        this.contactInput = "";
                        this.log("Der Kontakt konnte erfolgreich erkannt werden. Manuelle Bearbeitungen können im rechten Teilfenster vorgenommen werden.","info")
                    } else {
                        switch (response.status) {
                            case 400:
                                let json = await response.json();
                                this.log("Der Kontakt konnte nicht vollständig erkannt werden. Teilweise erkannte Felder sind eingetragen. Eine manuelle Fertigstellung ist notwendig. \n Eingabe:  <b>" + this.contactInput + "</b>", "info");
                                this.contact = json;
                                this.contactSave;
                                this.contactInput = "";
                                break;
                            case 404:
                                this.log("Konnte keine Verbindung zum Backend aufbauen. Der angefragte Pfad ist nicht verfügbar. Gegebenenfalls wird eine inkompatible Version genutzt.","error");
                                break;
                            case 500:
                                this.log("Es ist ein interner Server-Fehler aufgetreten. \n Leider konnte die Anfrage nicht bearbeitet werden.","error");
                                break;
                            default:
                                this.log("Es ist ein unbekannter Fehler aufgetreten. Der Fehlercode lautet " + response.status + ". \n Leider konnte die Anfrage nicht bearbeitet werden.","error"); 
                        }
                    }
                } catch(e){
                    this.log("Es konnte keine Verbindung zum Backend hergestellt werden. <br> Leider konnte die Anfrage nicht bearbeitet werden.","error");
                }
                
            },
            // Setzt die durch den Nutzer vorgenommenen Änderungen zurück
            discardChanges: function() {
                this.deletelog();
                if(JSON.stringify(this.contact) === JSON.stringify(this.contactSave))
                {
                    this.contact = JSON.parse(JSON.stringify(this.emptyContact)) 
                } else {
                    this.contact = JSON.parse(JSON.stringify(this.contactSave)) 
                }
            },
            // Speichert die Änderungen ab und überträgt den Kontakt in die Liste der fertigen Kontakte. Setzt danach die Eingabefelder und den zur Wiederherstellung gesicherte Version zurück
            applyChanges: function(){
                this.deletelog();
                if (JSON.stringify(this.contact) === JSON.stringify(this.emptyContact)) 
                {
                    this.log("Eine leere Eingabe kann nicht abgespeichert werden", "info");
                } else {
                    this.allContacts.push(this.contact);
                    this.contact = JSON.parse(JSON.stringify(this.emptyContact));
                    this.contactSave = JSON.parse(JSON.stringify(this.emptyContact)) 
                }
            },
            sendTitle: async function(){
                if(this.titleInput.lenght < 2)
                {
                    this.log("Der Titel ist zu kurz zum anlernen.", "error");
                } else {
                    this.deletelog();
                    try {
                        let response = await fetch("http://localhost:5000/title-learning", {
                            body: JSON.stringify({newTitle: this.titleInput}),
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                            }
                        });
                    
                        if (response.ok) { // wenn der HTTP-Status 200-299 ist (aktuell kommt nur 200)
                            this.log("Der Titel wurde gelernt. In zukünftigen Anfragen wird dieser erkannt.","info");
                            this.titleInput = "";
                        } else {
                            this.log("Der Titel wurde nicht gelernt.","error");
                        }
                    } catch(e){
                        this.log("Es konnte keine Verbindung zum Backend hergestellt werden. <br> Leider konnte die Anfrage nicht bearbeitet werden.","error");
                    }
                }
            },
            // Test, der die Eingabefelder füllt (also eine erfolgreiche Anfrage an das Backend simuliert) {Kann mit app.exampleContact() in der Konsole aufgerufen werden.}
            exampleContact: function() {
                this.contact = {
                    gender: "Männlich",
                    salutation: "Herr",
                    letterSalutation: "Sehr geehrter Herr Dr. Aiwanger",
                    title: "Dr.",
                    firstName: "Hubert",
                    lastName: "Aiwanger"
                };
                this.emptyContact = JSON.parse(JSON.stringify(this.emptyContact));
            }
        },
});
