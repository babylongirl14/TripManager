from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import TripViewSet, ActivityViewSet, login_view

router = DefaultRouter()
router.register(r'trips', TripViewSet, basename="trips")
router.register(r'activities', ActivityViewSet, basename="activities")

urlpatterns = [
    path('', include(router.urls)),
    path('login/', login_view, name="login"),
]
