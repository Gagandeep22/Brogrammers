from django.contrib import auth
from django.http import HttpResponseRedirect
from django.shortcuts import render, redirect
import pyrebase
from django import forms
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

# Create your views here.

class login(View):
    template_name = 'login/login.html'

    # display blank form
    def get(self, request):
        return render(request, self.template_name)

    # add to database
    def post(self, request):
        username = request.POST['username']
        password = request.POST['password']
        admin = db.child('Admin').get().val()
        aduser = admin['email']
        adpass = admin['password']
        if username == aduser and password == str(adpass):
            return HttpResponseRedirect('/home')
        else:
            raise forms.ValidationError('Looks like a username with that email or password already exists')

def logout(request):
    auth.logout(request)
    return redirect('login:login')