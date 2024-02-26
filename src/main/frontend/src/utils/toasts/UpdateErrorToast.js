import { toast} from 'vue3-toastify';

export function updateErrorToast (texto) {
  toast.error('Não foi possível ALTERAR esse(a) ' + texto, {
    autoClose: 5000
  });
}