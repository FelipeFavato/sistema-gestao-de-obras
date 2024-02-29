import { toast} from 'vue3-toastify';

export function deleteErrorToast (texto) {
  toast.error(texto, {
    autoClose: 5000
  });
}