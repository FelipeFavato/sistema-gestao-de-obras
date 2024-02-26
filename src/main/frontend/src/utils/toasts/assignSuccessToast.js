import { toast} from 'vue3-toastify';

export function assignSuccessToast () {
  toast.success('Sócio(a) ASSOCIADO(A) com sucesso!', {
    autoClose: 3000
  });
}