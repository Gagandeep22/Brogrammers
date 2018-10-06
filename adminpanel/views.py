from django.shortcuts import render, redirect
import pyrebase
from django.views import View

config = {
    'apiKey': "AIzaSyC-Ile_oqnvyZKO4aEHNp7J7BNu8lDvVoM",
    'authDomain': "voiceofmumbai-8ca1f.firebaseapp.com",
    'databaseURL': "https://voiceofmumbai-8ca1f.firebaseio.com",
    'projectId': "voiceofmumbai-8ca1f",
    'storageBucket': "voiceofmumbai-8ca1f.appspot.com",
    'messagingSenderId': "724490822942"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

class Home(View):
    template_name = 'adminpanel/home.html'

    def get(self,request):

        grievances = db.child('grievances').get().val()
        tc = 0
        pending = 0
        resolved = 0
        flagged = 0
        for keys in grievances:
            tc += 1
            if grievances[keys]['spam'] > 0:
                flagged += 1
            if grievances[keys]['status'] == 'pending':
                pending += 1
            elif grievances[keys]['status'] == 'resolved':
                resolved += 1

        data = {'tc':tc, 'pend': pending, 'res': resolved, 'flag':flagged}

        return render(request, self.template_name, data)

class Pending(View):
    template_name = 'adminpanel/pending.html'

    def get(self, request):

        data = db.child('grievances').get().val()
        sendata = []
        for keys in data:
            if data[keys]['status'] == 'pending':
                sendata.append((data[keys]['pic_url'],data[keys]['description'],data[keys]['location'],data[keys]['user_name'],keys))

        return render(request, self.template_name, {'data':sendata})

    def post(self, request):

        db.child('grievances').child(request.POST['hid_input']).update({'status': request.POST['status']})
        return redirect('adminpanel:pending')


class Resolved(View):
    template_name = 'adminpanel/resolved.html'

    def get(self, request):

        data = db.child('grievances').get().val()
        sendata = []
        for keys in data:
            if data[keys]['status'] == 'resolved':
                sendata.append((data[keys]['pic_url'],data[keys]['description'],data[keys]['location'],data[keys]['user_name'],keys))

        return render(request, self.template_name, {'data':sendata})

    def post(self, request):

        db.child('grievances').child(request.POST['hid_input']).update({'status': request.POST['status']})
        return redirect('adminpanel:resolved')