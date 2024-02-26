import { toast} from 'vue3-toastify';

export function insertErrorToast (texto) {
  toast.error('Não foi possível INSERIR, pois esse(a) ' + texto + ' já existe.', {
    autoClose: 5000
  });
}