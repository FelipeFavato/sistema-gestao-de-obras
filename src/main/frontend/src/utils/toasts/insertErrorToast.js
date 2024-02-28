import { toast} from 'vue3-toastify';

export function insertErrorToast (texto) {
  toast.error(texto, {
    autoClose: 5000
  });
}