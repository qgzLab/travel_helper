from django import forms
class FileForm(forms.Form):
    name = forms.CharField()
    headImg = forms.FileField()