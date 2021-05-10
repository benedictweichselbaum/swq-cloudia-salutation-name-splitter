var app = new Vue({
    el: '#app',
    data:
        {
            page: "start", // Startseite
            pageHistory: [], // Speichert die Seitenhistorie, um zurücknavigieren zu können
            error: "",
            info: "",
            contactInput: "", // Hält die Eingabe
            contact: {
                gender: "Männlich",
                salutation: "Herr",
                letterSalutation: "Sehr geehrter Herr Dr. Aiwanger",
                title: "Dr.",
                gender: "m",
                firstName: "Hubert",
                lastName: "Aiwanger"
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
            contactSave: {

            },
            allContacts: [],
        },
	computed: {
        savePhrase: function() {
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
            switchPage: function (page) {
                this.error = "";
                this.info = "";
                this.pageHistory.push(this.page);
                this.page = page
            },
            goBack: function(){
                if(this.pageHistory.length > 0)
                {
                    if (this.page === "game") {
                        this.exitsession();
                    }
                    this.error = "";
                    this.info = "";
                    this.page = this.pageHistory[this.pageHistory.length - 1];
                    this.pageHistory.pop()
                }
            },
            log: function (log, logType){
                this.info = ""
                this.error = ""
                if (logType === "error" || logType === "Error"){
                    this.error = log
      
                } else if (logType === "info" || logType === "Info") {
                    this.info = log
                }
            },
            deletelog: function () {
                this.info = ""
                this.error = ""
            },
            // Sendet die an "contactInput" gebundene Telefonnummer an das Backend und schiebt diese in "phoneNumberOutput", welches diese anzeigt
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
                
                    if (response.ok) { // if HTTP-status is 200-299
                        let json = await response.json();
                        this.contact = json;
                        this.contactSave;
                        this.contactInput = "";
                        this.log("Der Kontakt konnte erfolgreich erkannt werden. Manuelle Bearbeitungen können im rechten Teilfenster vorgenommen werden.","info")
                    } else {
                        switch (response.status) {
                            case 400:
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
            checkContactold: function (){
                this.deletelog();
                if(this.contactInput.length < 2)
                {
                    this.log("Die Eingabe ist zu kurz", "error")
                    return;
                }
                fetch("http://localhost:5000/contact", {
                    body: JSON.stringify({contactString: this.contactInput}),
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    }
                }).then(response =>{
                    response.json().then(json =>{
                        console.log("Antwort empfangen: " + json)
                        phoneNumberOutput = json;
                        // Eingabefeld für nächste Kontakteigabe leeren
                        this.contactInput = "";
                    } )}   );
            },
            discardChanges: function() {
                this.deletelog();
                if(JSON.stringify(this.contact) === JSON.stringify(this.contactSave))
                {
                    this.contact = JSON.parse(JSON.stringify(this.emptyContact)) 
                } else {
                    this.contact = JSON.parse(JSON.stringify(this.contactSave)) 
                }
            },
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
            }
            
        },
});
